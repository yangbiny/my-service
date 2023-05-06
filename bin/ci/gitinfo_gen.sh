#!/bin/bash

set -e

echo "generate git info to rev-version.json"

rm -rf rev-version.json

build_desc=""

function gitinfo {
    if [ -d .git ]; then
        commitid=`git --no-pager log --pretty=format:'%H' -1`
        branch=`git branch -a --contains $commitid | grep -v "*"`
        branch=`echo $branch | sed 's/^remotes\///'`
        author=`git --no-pager log --pretty=format:'%an <%ae>' -1`
        remote=`git remote -v | grep fetch | awk '{print $2}'`

        build_desc=$branch'<br/>'$commitid'<br/>'$author

        echo "{"
        echo '"buildtime": "'`date "+%Y-%m-%d %H:%M:%S"`'",'
        echo '"branch": "'$branch'",'
        echo '"remote": "'$remote'",'
        echo '"commit": "'$commitid'",'
        echo '"author": "'$author'",'
        echo '"date": "'`git --no-pager log --pretty=format:'%ad' -1`'",'
        echo '"message": "'`git --no-pager log --pretty=format:'%s %b' -1 | sed 's/\"/\\\\"/g'`'"'
        echo "}"
    else
        echo "Not a git repository."
    fi
}

gitinfo > rev-version.json
echo 'build_desc:'$build_desc

echo "git info generate success"