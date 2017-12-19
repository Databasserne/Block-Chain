# Block-Chain

## Table of content
1. [Introduction](#introduction)
    1. [Authors](#authors)
    2. [Requirements](#requirements)
    3. [Overview](#overview)
2. [Run program](#run-program)
    1. [Quickstart](#quickstart)
    2. [Connect](#connect)
    3. [Commands](#commands)
        1. [Add block](#add-block)
        2. [Mining](#mine-block)
3. [Demonstraton](#demonstration)
4. [References](#references)

## Introduction
This blockchain is working in a P2P network and is made in Java.<br/>
It uses docker services and as default there's created 4 peers.

### Authors
* Jonas Simonsen
* Kasper Worm
* Alexander Steen
* Martin Karlsen

### Requirements
To use this you will need the following:
* Docker
* Maven
* Telnet

### Overview
The docker-compose file will from default, create 4 peers.
Each peer will have its own ip address and have been attached a port:
* Peer1: 6001
* Peer2: 6002
* Peer3: 6003
* Peer4: 6004

## Run program

### Quickstart
To quickly setup and run the system, go to the root folder and use the following:
```
./build-run.sh
```
This will start the build.sh file in the Peer, which will run the necessary maven commands.<br/>
The build-run file will also run the docker-compose which will set start the environment.

To run the test files, type the following:
```
./TestData.sh
```
```
./TestMine.sh
```

### Connect
When the system has been started, you are able to connect to the peers.<br />
You'll do that, using telnet.<br />
First thing you need to do is to find your docker ip.<br />
You can do that by typing
```
docker-machine ip
```
and copy the ip address.<br />
<b>(Note: this is only needed on windows machines, since localhost wont work. For linux and mac, the default setting will work.)</b>
<br/>
For connecting using tellnet, write:
```
telnet <docker-ip-address> <port>
```

### Commands
While connected to the single Peer, you can type some commands.

#### Add block
To add a block you will simply have to write:
```
peer add <{"data"}>
```
The data field is optional and will create a block with empty data.

#### Mine block
To mine a block, type:
```
peer mine <id> "<{"data"}>" <{-r}>
```
As, when using the add command, the data field will be optional, and will be empty if not set.<br />
For mining, there's an extra optional field <b>-r</b><br />
This tells the system to use the recursive mining function.<br/>
<b>(Note: the recursive mining, isn't actually recursive since this will end in a stackoverflow exception for must people.<br />
For now it will mine, the same way as the original mining, but will sleep, and therefore take more time.)</b>

## Demonstration
<b>Build and run</b>
![Build Run](https://github.com/Databasserne/Block-Chain/blob/master/Build-Run.png)

<b>Peer Add Docker</b>
![Peer Add Docker](https://github.com/Databasserne/Block-Chain/blob/master/Peer%20Add%20Docker.png)

<b>Peer Add Telnet</b>
![Peer Add Telnet](https://github.com/Databasserne/Block-Chain/blob/master/Peer%20Add%20Telnet.png)

<b>Peer Mine Docker</b>
![Peer Mine Docker](https://github.com/Databasserne/Block-Chain/blob/master/Peer%20Mine%20Docker.png)

<b>Peer Mine Telnet</b><br/>
![Peer Mine Telnet](https://github.com/Databasserne/Block-Chain/blob/master/Peer%20Mine%20Telnet.png)

## References
[https://github.com/lhartikk/naivechain](https://github.com/lhartikk/naivechain)
