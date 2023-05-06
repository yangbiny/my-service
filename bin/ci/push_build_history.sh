#!/bin/bash

set -e

PROJECT=$1

if [ -d .git ]; then
  commitid=`git --no-pager log --pretty=format:'%H' -1`
  branch=`git branch -a --contains $commitid | grep -v "*"`
  branch=`echo $branch | awk '{print $NF}' | sed 's/^remotes\///'`
  author=`git --no-pager log --pretty=format:'%an <%ae>' -1`
  remote=`git remote -v | grep fetch | awk '{print $2}'`

  curl 'https://phoenix.duitang.net/api/v1/pipelines/buildhistory' --data "project=$PROJECT&remote=$remote&branch=$branch&author=$author&hash=$commitid&buildno=$BUILD_NUMBER" --compressed
else
  echo "Not a git repository."
fi

