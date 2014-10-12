# XSP 1.0 [![Build Status](https://travis-ci.org/m1kc/xsp.svg?branch=master)](https://travis-ci.org/m1kc/xsp)

XSP is an open eXtensible Socket-based Protocol and XSP 1.0 is its implementation written on Java.

## Features

* Text messaging
* File transfer (it is very high-speed because connection is direct)
* Voice chat and sound streaming
* Cooperative drawing
* Controlling the remote desktop (experimental)

## Building and running

For some technical reasons, sources are distributed as a NetBeans project. But you won't need NetBeans in the most cases. In fact, it's only needed to edit GUI, in other cases you can use any text editor you'd like to.

Requirements:

* Java
* J2ME SDK
* Ant

The simplest build method is to use NetBeans.

To build it using Ant, just type:

    ant jar

To run it:

    java -jar dist/XSP.jar

Or you can just double-click on `XSP.jar` (it will be placed in the `dist` folder) if your desktop environment is smart enough.
    
To run it in developer mode (which allows you to open UI without establishing a connection, to emulate packet receiving and to use experimental features):

    java -jar dist/XSP.jar --developer
    
Alternatively, you can use Ant. In this case, developer mode will be turned on automatically.
    
    ant run

## Authors

* m1kc
* Solkin

## Other stuff

Protocol specs are available on the wiki:
https://github.com/m1kc/xsp-1.0/wiki

See also:
http://sourceforge.net/projects/m1kc-xsp/

We use Glyphicons (http://glyphicons.com).
