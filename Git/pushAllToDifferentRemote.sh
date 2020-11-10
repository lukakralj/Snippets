#!/bin/bash

echo "Pushing repository to the new remote..."
if [ -z "$1" ]
	then
		echo "Please specify the URL of the new remote."
		exit 1
fi

git remote rename origin upstream &&
git remote add origin $1 &&
git push REMOTE '*:*' &&
git push REMOTE --all &&
git push REMOTE --tags &&
echo "Remote changed and updated successfully."
