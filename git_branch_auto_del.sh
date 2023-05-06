#!/bin/bash

# sync to remote
git remote prune origin && git fetch


# 1. delete local branch
git branch | grep -i -v master | grep -i -v main | xargs -n1 git branch -d

# 2. delete remote branch which has been merged
git branch -r --merged | grep -i -v master | grep -i -v main | awk -F 'origin/' '{print $2}' | xargs -n1 -I% git push origin --delete %
