#!/bin/sh
cd "$(dirname "$0")" || exit 1
python3 tools/manage.py run
result=$?
if [ "$result" -ne 0 ]; then
    printf '\n启动失败，按回车关闭窗口。\n'
    read -r unused
fi
exit "$result"
