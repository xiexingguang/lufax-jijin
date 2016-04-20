ps -ef | grep prj | grep -i "$1" | grep -v grep | tr -s "[:blank:]" | cut -d" " -f 2 | xargs -t kill -9
