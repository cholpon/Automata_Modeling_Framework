Automata Modeling Framework
Build instructions:
1. Download and install Eclipse SDK.
Currently the latest release could found here:
http://download.eclipse.org/eclipse/downloads/drops/R-3.6.1-201009090800/index.php
2. Install Graphical Editing Framework GEF SDK.
From the Eclipse main menu: Help/Install New Software
- Choose “All Available Sites” in the work with field.
- Type GEF in the type filter text field. A list should appear.
- Select “Graphical Editing Framework GEF SDK” from the list.
- Use default options on every step of installation.
3. Un-pack the zip-archive with the source code.
4. Start Eclipse SDK and choose a workspace (some location on the disk).
5. Create a new plug-in project.
From the Eclipse main menu: File/New/Plug-in Development/Plug-in Project
- Set the Project Name
- Remove the flag “Use default location” and browse for the location where you have
putted the un-packed source code. Choose the “amf” folder.
- Go next to the “content” part. Use the default options there.
- Go next to the “templates” part. Remove the flag “create a plug-in using one of the
templates”.
- Press “finish” and click “yes” to open the appropriate perspectives.
6. Check, that the Built Automatically option is selected. From the Eclipse main menu:
Project/ Built Automatically.
7. To run the plug-in right-click on the root project dir at the package explorer.
And choose Run as/Eclipse application.
