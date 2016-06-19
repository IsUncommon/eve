#!/bin/sh

chmod 777 ././quality/git-pre-push.sh
cp ././quality/git-pre-push.sh ./.git/hooks/pre-push

echo pre-push setup complete.
