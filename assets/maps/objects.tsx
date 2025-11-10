<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="objects" tilewidth="32" tileheight="32" tilecount="2" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="GameObject">
  <properties>
   <property name="animation" value="IDLE"/>
   <property name="animationSpeed" type="float" value="1"/>
   <property name="atlasAsset" value="OBJECTS"/>
   <property name="speed" type="float" value="4"/>
  </properties>
  <image source="objects/person.png" width="32" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="10.4161" y="19.3201" width="10.2061" height="3.73802">
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="1" type="GameObject">
  <properties>
   <property name="animation" value="IDLE"/>
   <property name="animationSpeed" type="float" value="0"/>
   <property name="atlasAsset" value="OBJECTS"/>
   <property name="speed" type="float" value="0"/>
   <property name="z" type="int" value="0"/>
  </properties>
  <image source="objects/backpack.png" width="32" height="32"/>
 </tile>
</tileset>
