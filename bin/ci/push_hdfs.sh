#!/bin/bash
set -e

echo "push hdfs $*"

hdfs_url="http://ci.hdfs.duitang.net:9870"
hdfs_ci_url=$hdfs_url/"webhdfs/v1/apps/ci"

# create folder
curl -i -X PUT "$hdfs_ci_url/$JOB_NAME/$BUILD_NUMBER?op=MKDIRS&user.name=admin"

for file in "$@"
do
    echo "$file"
    md5file="$file".md5
    md5sumfile="$file".md5sum
    md5sum $file > $md5file
    md5sum $file | awk '{print $1}' > $md5sumfile

    filename=`basename $file`
    md5filename=`basename $md5file`
    md5sumfilename=`basename $md5sumfile`

    # create file
    echo "push $file to $hdfs_zip_location"
    hdfs_file_location=`curl -i -X PUT "$hdfs_ci_url/$JOB_NAME/$BUILD_NUMBER/$filename?op=CREATE&overwrite=false&user.name=admin" | grep Location | awk '{print $2}' | sed 's/\s*//g'`
    curl -i -X PUT -T "$file" -vvv "$hdfs_file_location"

    # create md5sum
    hdfs_md5_location=`curl -i -X PUT "$hdfs_ci_url/$JOB_NAME/$BUILD_NUMBER/$md5filename?op=CREATE&overwrite=false&user.name=admin" | grep Location | awk '{print $2}' | sed 's/\s*//g'`
    curl -i -X PUT -T "$md5file" -vvv "$hdfs_md5_location"

    hdfs_md5_location=`curl -i -X PUT "$hdfs_ci_url/$JOB_NAME/$BUILD_NUMBER/$md5sumfilename?op=CREATE&overwrite=false&user.name=admin" | grep Location | awk '{print $2}' | sed 's/\s*//g'`
    curl -i -X PUT -T "$md5sumfile" -vvv "$hdfs_md5_location"
done
