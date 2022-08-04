# GameEngineReferenceProjects
I tend to not stick with a single game engine, so this repository is a collection of simple starter projects for various game engines done with my own style rather than following someone else's tutorials to jog my memories, perhaps others will find the reference useful as well.

Definitely just starting points here. For the most part these projects just get a character moving around the scene in the way that made the most since to me after examining documentation on: which rendering library provided the parent classes to form the basis of the pipelines (they're almost all OpenGL), which physics engine provided the parent classes to form the basis of the interaction of forces in their simulations (most of these have bullet as default and are comapatible with bullet if it's not the default), and what datastructure provides the primary organization for the scene heirarchy (seems to be an even split between parent/child and trees for the most part in these engines).

The files on this repository are reference material for starting points, and certainly not fully debugged games.


<h2>PyGame</h2>
If it's a 2D game and speed is not of the upmost concern why make it any more difficult than it needs to be...

OpenGL libraries/modules and Physics Bullet engine are included in the install for 3D games, but you have to do it all from scratch.


<br><br>

<h2>Panda3D</h2>
Ease of use of Python game development, but it's actually a c-wrapper, so decent speed. The rendering pipeline was created for a cartoonish style game by disney, but sometimes that's enough.

For models, export a .glb from blender then use the custom python module "gltf2bam" to convert it to a .bam model.

<br><br>

<h2>jMonkey</h2>
Great rendering pipeline for desktop, URL download-and-run, iOS, Android, and VR games. Thorough extensions of Bullet Physics and OpenGL to make the game engine, so they stuck with the standards, and followed Java coding conventions wonderfully for all the libraries --including JavaDoc pages (which I preferred to look at over their official tutorials and forums before long at all).

Full code based (download the libararies and include in a project) or GUI based (jMonkeyEngine SDK is an extension of the Apache Netbeans IDE). Great terrain builder in the GUI, and everything on the interface can still be easily and efficienctly accessed from the code (it's netbeans so there's a drag and drop text insertion pallete as well that can sometimes help for quick reference material).

The scene/game hierarchy (this is by far the easiest engine to keep all stats moving level to level) is a tree structure, so at least in my mind it made sense to make all custom game objects a subclass of their Node. Since Java only passes by reference, accessing things like the asset and input managers from within any particular object is still the original for the game so memory consumption grows very slowly as the games get large. In short there are a lot of internal and external things about jMonkey that make it an outstanding option for game development.

Export a .gltf file from blender for models (no .glb use either a gltf+bin+texture or an embedded .gltf file) and importing into the SDK will automatically convert it to the .j3o file (that is an 'o' not a zero, I have made that typo so many times).

<br><br>

<h2>GoDOT</h2>
By far the best option if you're a linux user... more coming when a nice clean project for here is ready...



<br><br>

<h2>Unity</h2>
Kind of the standard these days, this is what I usually use in the game development courses I teach. Once they move stuff into their user interface though it often becomes cumbersome and sloppy-in-memory to get control back for it in the scripts without a lot of overhead memory management code (which is no longer cross-platform). I really like Unity, but every new release seems to have something new for me to not like about the engine, don't know how much longer it will be before the things I don't like outweigh the things I do.

By far the best online community for support and tutorials. Multiple rendering pipelines and physics engines extended to their own custom classes to optimize per project without having to learn new syntax "patterns" or general game structuring. Builds for about everything you can name that a game engine should build for. And arguably the second best rendering pipeline at the time I'm writing this. 

The scene heirarchy is a parent/child while the overall game heirarchy is a graph, all custom game objects must be a subclass of Unity's starting point. Otherwise layers and collision groups from bullet physics, fully configurable input manager (though it's probably already configured for about any input device you can name).

(( please ignore the quality of animations here, I had to make and then use these while talking and under the time constraints of a classroom lecture, I was only describing the proccess here, i'll try and update the imagery at some point in the future ))

<br><br>
<h2>Unreal</h2>
Without a doubt the best rendering pipeline available and coded in C++ to give full control over everything to the game developer... There's not really a quick start to Unreal, but I might get something on here someday...

<br><br>
