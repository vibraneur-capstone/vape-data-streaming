#!/bin/bash
args=("$@")
len=${#args[@]}

artifact_bucket=s3://vape-artifacts/vape-data-streaming
artifact_dest=/app
echo "Deployment starting..."
sleep 3
if [ "$len" == 0 ]; then
  echo Default to the latest built jar
  target=streaming-dist.zip
else
  target=streaming-$1.zip
fi

echo downloading from $artifact_bucket/"$target"
sudo aws s3 cp $artifact_bucket/"$target" $artifact_dest

echo download complete...
sleep 3

echo restarting service...
sudo systemctl restart streaming

echo "waiting..."
sleep 10
sudo systemctl status streaming