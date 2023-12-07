#!/bin/sh
quarkus build --native && mv target/jodu-*-runner  ~/bin/jodu

