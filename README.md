## Mind Notes: A mind mapping tool for Chromium OS

### Introduction
Mind mapping tool is a piece of software that allows the user hierarchical diagrams centered around a single word or concept. They can be used for taking notes on a lecture, outlining project ideas or just organizing one's thoughts. I propose a simple HTML5-based editor with a minimal interface, focused on speed of creation of useful mind maps. It would more likely be used for a quick brainstorming session than to prepare visuals for company's next big keynote.

### Goals:
* To design a simple tool for taking notes using the mind map approach;
* Find a balance between UI simplicity and UI usage speed;
* Make use of HTML5 goodies out there: most notably local storage, app cache.
* Optimize UX for Chromium OS (make it an installable web app, design UI with the chrome browser window around in mind).

### Non-goals:
* To make a full-blown diagram editor, like Gliffy or MS Visio.

For a more detailed description of the concept, take a look at:

[https://docs.google.com/document/d/1lpan-OO6X7Lfl694ItMEAUoG_nn_0KIP1a2jznqlHXs/preview](GSoC Project Proposal)

### What does this has to do with Chromium OS?
I work on this project as Google Summer of Code 2010 participant working for Chromium OS. I answered a call from http://code.google.com/p/chromium-os/wiki/GoogleSummerOfCode2010 to design web apps targeted primarily for Chromium OS platform. Mind Notes, despite being code-wise a regular HTML5 web app, is designed with primarily with Chromium OS in mind.

### How will Chromium OS benefit from such an app?
Taking notes using a computer is a serious challenge. However, it is an attractive and obvious use case for a netbook. A simple, fast mind mapper with cloud storage running on a netbook would be a great on lectures, during business meetings or learning. Having such an app on your Chromium OS box could greatly improve it’s real-life applications. Although it is not my ambition to solve the problem of note-making on a computer alone, I think this app is a step in the right direction; an interesting UX experiment, if you may. Comments on current version: This version shows which mechanics work as of today. Traditionally, no single thing is final. Tragically many features you’d expect aren’t there, but you can see the general direction of the app. When you log in, on your left there’s a sidebar listing thing you can play with. Why do I have to log in? Mind Notes uses Google App Engine for storing your mind maps in the cloud. To identify which documents were yours, your login is used.

### How can I contribute?
I would greatly appreciate any feedback on the app. Here’s some ideas:

* Tell me what features would you like to see in the finished product;
* If you expected some to just be there and was surprised that it is missing, tell me :)
* If you see a bug, tell me :)
* Take a look at the source code. Tell me why it’s bad.
* If you want to see a new feature or want to report a bug, feel free to spam the issue tracker.


Automatically exported from code.google.com/p/mind-notes
