# XSP 1.0 [![Build Status](https://secure.travis-ci.org/m1kc/xsp-1.0.png?branch=master)](http://travis-ci.org/m1kc/xsp-1.0)

XSP is an open extensible socket-based protocol and XSP 1.0 is its implementation written on Java.

## Building XSP

The simpliest build method is to use NetBeans.

To build it using Ant, type:

    ant jar

To run it:

    java -jar dist/XSP.jar
    
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

