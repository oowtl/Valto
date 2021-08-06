# Install Kurento..!











# Errors



## bash: vi: command not found

- 상황

  - 파일을 수정하려고 하는데 vi 라는 편집기를 인식하지 못할때

    ```ubuntu
    root@docker-desktop:/# vi /etc/kurento/modules/kurento/WebRtcEndpoint.conf.ini
    bash: vi: command not found
    ```

    ```ubuntu
    root@docker-desktop:/etc/kurento/modules/kurento# vi WebRtcEndpoint.conf.ini
    bash: vi: command not found
    ```

- 해결

  1. 도커 내부에서 OS 확인하기

     - ```ubuntu
       root@docker-desktop:/etc/kurento/modules/kurento# cat /etc/*-release
       DISTRIB_ID=Ubuntu
       DISTRIB_RELEASE=16.04
       DISTRIB_CODENAME=xenial
       DISTRIB_DESCRIPTION="Ubuntu 16.04.7 LTS"
       NAME="Ubuntu"
       VERSION="16.04.7 LTS (Xenial Xerus)"
       ID=ubuntu
       ID_LIKE=debian
       PRETTY_NAME="Ubuntu 16.04.7 LTS"
       VERSION_ID="16.04"
       HOME_URL="http://www.ubuntu.com/"
       SUPPORT_URL="http://help.ubuntu.com/"
       BUG_REPORT_URL="http://bugs.launchpad.net/ubuntu/"
       VERSION_CODENAME=xenial
       UBUNTU_CODENAME=xenial
       ```

       - Ubuntu 라면 apt 를 사용해서 vim 을 설치하자

  2. vim 설치하기

     1. ```ubuntu
        apt-get update
        ```

     2. ```ubuntu
        apt-get install vim
        ```

  3.  vim 명령어를 통해서 수정해보자!

