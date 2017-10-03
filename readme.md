
hello world java webapp
=======================

this is a simple web app that cna be used to test a ci/cd pipeline.
it builds a webapp that just returns some html that says "hello world"

* [Hello World](http://localhost:8080)
* [Hello Remi](http://localhost:8080?name=Remi)

This image also contains a Dockerfile to create a docker image.
An additional batch file `build.bat` in the docker directory automates the build
(invoked from the top-level build.bat).
If you like to create your own image in your own repo, you must update the environment
variables `DOCKER_REPO` and `IMAGE_NAME` in the build.bat file.

__DISCLAIMER:__ There are no guarantees on this software - use it at your discretion, but don't count on anybody
supporting it. If you have any suggestions for enhancements, you can file a pr, but I reserve the right
to silently ignore it!

last modified: 2017-10-03 17:21:00
