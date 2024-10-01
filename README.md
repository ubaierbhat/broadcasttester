# BroadcastTx

BroadcastTx is a simple Android app built using **Kotlin** and **Jetpack Compose**. This app allows users to create and send custom Android broadcasts without using **ADB** commands.

## Features

- **Custom Broadcast Action**: Allows users to specify the broadcast action.
- **Custom Component Name**: Allows users to specify the package name and receiver class name.
- **Add Extras**: Users can add different types of extras (e.g., String, Long, Boolean) to the broadcast.
- **Remove Extras**: Added extras can be removed before sending the broadcast.
- **Send Broadcast**: Once the action, component, and extras are configured, users can send the broadcast with a single click.

## Purpose

This app is designed to make it easier to test Android broadcast receivers by simulating the functionality of the `adb shell am broadcast` command, providing a graphical interface for developers to customize and send broadcasts without using the command line.

The equivalent ADB command for a broadcast might look like this:

```bash
adb shell am broadcast -a com.app.org.xyz.ACTION -n com.app.org.m/com.app.org.receiver.SinfoReceiver --es keyname Value
