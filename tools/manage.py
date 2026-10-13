#!/usr/bin/env python3
"""Build and run the preserved 2022 coursework using the standard library."""
import os
from pathlib import Path
import re
import shutil
import subprocess
import sys
from zipfile import ZipFile, ZIP_DEFLATED

ROOT = Path(__file__).resolve().parent.parent


def java_tool(name):
    homes = []
    if os.environ.get('JAVA_HOME'):
        homes.append(Path(os.environ['JAVA_HOME']))
    if sys.platform == 'darwin':
        result = subprocess.run(['/usr/libexec/java_home', '-v', '14+'], capture_output=True, text=True)
        if result.returncode == 0:
            homes.append(Path(result.stdout.strip()))
        homes.append(Path('/Applications/IntelliJ IDEA.app/Contents/jbr/Contents/Home'))
    candidates = [p / 'bin' / (name + ('.exe' if os.name == 'nt' else '')) for p in homes]
    found = shutil.which(name)
    if found:
        candidates.append(Path(found))
    for candidate in candidates:
        if not candidate.is_file():
            continue
        probe = subprocess.run([str(candidate), '-version'], capture_output=True, text=True)
        version = re.search(r'(?:version\s+")?(\d+)(?:\.(\d+))?', probe.stdout + probe.stderr)
        if probe.returncode == 0 and version:
            major = int(version[2]) if version[1] == '1' else int(version[1])
            if major >= 14:
                return str(candidate)
    raise RuntimeError(f'需要 Java 14+ 的 {name}；请将 JAVA_HOME 指向相应 JDK。')


def build():
    classes = ROOT / 'build/classes'
    if classes.exists():
        shutil.rmtree(classes)
    classes.mkdir(parents=True)
    sources = sorted((ROOT / 'src').rglob('*.java'))
    compiler = java_tool('javac')
    subprocess.run([compiler, '--release', '14', '-encoding', 'UTF-8', '-d', str(classes), *map(str, sources)], cwd=ROOT, check=True)
    with ZipFile(ROOT / 'CircleKnight.jar', 'w', ZIP_DEFLATED) as jar:
        jar.writestr('META-INF/MANIFEST.MF', 'Manifest-Version: 1.0\r\nMain-Class: Windows.StartMenu\r\n\r\n')
        for item in sorted(classes.rglob('*.class')):
            jar.write(item, item.relative_to(classes).as_posix())
    print(f'已编译 {len(sources)} 个源文件，生成 CircleKnight.jar（Java 14 字节码）', flush=True)


def check():
    paths = set()
    for source in (ROOT / 'src').rglob('*.java'):
        for value in re.findall(r'"([^"\n]*\.(?:png|jpg))"', source.read_text(encoding='utf-8')):
            paths.add(Path(value).as_posix())
    missing = sorted(p for p in paths if not (ROOT / p).is_file())
    if missing:
        raise RuntimeError('缺失图片：' + ', '.join(missing))
    folder = ROOT / 'build/check'
    folder.mkdir(parents=True, exist_ok=True)
    source = folder / 'AssetCheck.java'
    source.write_text('''import javax.swing.ImageIcon;
public class AssetCheck {
    public static void main(String[] args) {
        for (String path : args) {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
                throw new IllegalStateException("Cannot decode " + path);
            }
        }
        System.out.println("Decoded " + args.length + " referenced images successfully.");
    }
}
''', encoding='utf-8')
    subprocess.run([java_tool('javac'), '--release', '14', '-d', str(folder), str(source)], check=True)
    subprocess.run([java_tool('java'), '-Djava.awt.headless=true', '-cp', str(folder), 'AssetCheck', *sorted(paths)], cwd=ROOT, check=True)


def main():
    action = sys.argv[1] if len(sys.argv) > 1 else 'run'
    if action == 'build':
        build()
    elif action == 'check':
        build()
        check()
    elif action == 'run':
        if not (ROOT / 'CircleKnight.jar').is_file():
            build()
        java = java_tool('java')
        print(f'Java: {java}\n资源目录: {ROOT}', flush=True)
        return subprocess.call([java, '-jar', 'CircleKnight.jar'], cwd=ROOT)
    else:
        raise RuntimeError('用法：python3 tools/manage.py [run|build|check]')
    return 0


if __name__ == '__main__':
    try:
        sys.exit(main())
    except (RuntimeError, subprocess.CalledProcessError) as error:
        print(error, file=sys.stderr)
        sys.exit(1)
