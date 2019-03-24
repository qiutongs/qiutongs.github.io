---
title: "Remote Development with Clion on EC2 Ubuntu"
---

## Overview

Sometimes people have to develop on Linux environment. And sometimes VM is not a good option.
So here is where remote development fits in. Basically, it means developer can write code locally
on any OS(macOS, windows...), and the code is copied to remote linux machine for compile and execution.

This article is to share my experience of Clion and EC2 Ubuntu

1. Create a EC2 Ubuntu instance
2. Setup Clion IDE for remote Development

## Create a EC2 Ubuntu instance

0. Choose a proper region that is close to you.
1. Choose an Amazon Machine Image (AMI)
- Ubuntu Server 18.04 LTS (HVM), SSD Volume Type
- 64-bit x86
2. Be careful with the security group. It at least needs to allow inbound port for "SSH TCP 22"
3. Download the private key "<file name>.pem" and test the connectivity
```
ssh -i <file name>.pem ubuntu@<host public DNS>
```

**Note:** The username for Ubuntu is "ubuntu"

Now, ssh to the host.

## Confirm sftp and rsync

```
sftp --verison
rsync --Version
```
## Install Development Tools

```
sudo apt install make, cmake, gcc, g++, gdb, cmake
```

## Configure Clion

- open Settings/Preferences | Build, Execution, Deployment | Toolchains
- create a new Toolchain
- select remote host
- "Authentication type": Key pair (OpenSSH or Putty); Private key file: <file name>.pem

## Ref

- https://blog.jetbrains.com/clion/2018/09/initial-remote-dev-support-clion/
- https://www.jetbrains.com/help/clion/creating-a-remote-server-configuration.html
