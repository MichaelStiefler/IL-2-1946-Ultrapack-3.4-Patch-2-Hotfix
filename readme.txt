Ultrapack 3.4 "Cassie" Patch 2 Hotfix 24
========================================

To install this hotfix, run the executable, point it at your Ultrapack 3.4 Patch 2 game folder and let the installer do it's job.

This hotfix addresses these issues encountered after release of the previous Hotfix:
* Custom per-mission view settings from .mis file's [Mods] section fixed, kindly refer to the lower half of this post for available settings: https://www.sas1946.com/main/index.php/topic,67406.msg736798.html#msg736798
* Rare case fixed where the newly implemented removal of aircraft from a group in case of lethal damage would cause AI to trigger a couple of errors in the log - as an additional safety measure, airplanes removed from a group due to damage form their own group now.
* Very odd case of explosion effects not being able to render on land/water surface covered, corresponding nullpointer checks added

New features:
* Bomb Loadouts added to C-47 for standing use (Egypt-Israel 1948, Football War 1969) (introduced with Hotfix 21).

Furthermore, these issues that have been addressed by previous hotfixes since the release of Patch 2 are included in this cumulative Hotfix as well:
* Issue fixed which caused buildings to disappear from maps
* Li-2 Top Gunner fixed (hook name, green fuselage)
* Issues fixed which would prevent player aircraft from loading (compass issue)
* Plates are dealt with properly in HouseManager now (4.15.1 backport)
* AI planes not belonging to a group (anymore) fixed.
* Fi-156 SuperDuper mod sound issues fixed
* 270 compasses fixed/changed. Almost all cockpits affected (693 cockpits in total). Prepare for bugs, none of these changes have been tested.
* AI pilots don't keep flying planes with fuel tanks in bright flames anymore
* new conf.ini parameter "logtimeoverflow" - "0" hides all such messages, "1" (default) shows them, but limits the messages to max. 1 per second, "2" shows all time overflow messages (old behaviour)
* A-26B loadout errors fixed
* P-61 pink plane fixed
* J8M3
* Avia S-199 and HA-1112 missing canopy fixed
* "Blackouts and Redouts" difficulty settings doesn't influence controllability of planes after bail out anymore
* Fw-187 a-0 and a-1 canopy issues addressed
* P-47 M, P-47 D30, P-47 D40 lod issues addressed
* B-339 and B-239 issues addressed (don't ask me which...)
* Yer-2 - something fixed (bug report was incomplete and so is my memory)
* M6A1 Tail alignment fixed
* Me-262 HG-Vb and TPF canopy issues addressed
* A2N2, A2N3 issues addressed
* Kurogane... fixed something, can't remember what. Bug reporter didn't bother to provide any details and so do I.
* Some static objects fixed
* Page up/down keys fixed in FMB
* P-61 some cockpit related sh*t fixed. Bug reporter just provided a log with no comment, I provide a fix with no comment.
* No Player Map Icon behaviour fixed
* fw-190 v13 tail fixed
* Logfile default settings can be overruled by adding "OverrideLogSettings=1" to the [Console] section of conf.ini
* Fw-187 Fuel Gauge fixed
* B-26B wheel brakes fixed
* UTI-4 flaps fixed
* Ju-88 Mistel Bang restored
* Ki-98 II/III loadouts fixed
* F6F-5N, BlenheimS1, BlenheinMkIVlate properties fixed
* J8M1, J8M3, J9M1-K loadouts fixed
* Bf 109 B-1, Bf 109 B-2, Bf 109 C-1, Bf 109 D-1, Bf 109 D-1 Late canopy rendering on bailout fixed
* Il-2M Late gunner helmet default texture fixed
* Bf 109 G-2 gondola loadouts fixed
* Martin B-26B-35 Marauder fixed clips for gear and tail navlight, resized textures for internals (thanks to SAS~Loku)
* A2N: fixed clips, cleaned meshes, fixed shadows and collisions (thanks to SAS~Loku)
* A4N: fixed clips, cleaned meshes, fixed shadows and collisions (thanks to SAS~Loku)
* Fw 190F-8 canopy fixed (thanks to SAS~Loku)
* Polish planes: All sorts of issues fixed (thanks to SAS~Loku)
* Cockpit Door bug fixed
* F-84G introduction date fixed
* IL-4 gunner texture bug fixed
* P-35 propeller animation fixed
* "Static Spawn" Trigger net replication issue fixed
* Trigger messages shown off to the left in case of multi-line messages: fixed
* 79 buildings with various issues have been identified and fixed, some of them using "stand-in" dead meshes
* 12 "stationary" (ships, tanks, cars, airplanes etc.) objects with mesh issues have been fixed
* SM.81 series gunner issues fixed
* BP Defiant net flight model and loadout declaration issues fixed
* Ta 152 canopy lever animation fixed
* Fw 190F-1 texture bug fixed
* Fw 190V-29 flaps position fixed
* Breda Ba.65 Biposto fixed
* F-72A canopy moving depending on AoA fixed
* He-111 H-5 loadout typo fixed
* Bf 109G-6 Early and Late gunsight filter animation fixed
* G4M tail turret meshes correctly reflecting RL ones now
* G4M bomb bay doors and bomb locations fixed
* Missing stationary PBY fixed
* Hawker Hurricane Mk. II Russian version ShVAK ejection ports fixed
* H5Y-1 loadouts fixed
* G3M Nell flap animations fixed
* De Havilland Mosquito flight models adjusted
* Fw-190A-2 Flaps detached: Fixed (And with it, all other 190s)
* XF5U ejection seat: Fixed (And with it, a couple of others)
* Bf 109 canopy during bale out: Fixed (And with it, all 109 source codes got rewritten and streamlined)
* Ki-84 canopy holes: Fixed (And with it, 18 other airplanes)
* Ju-88C6 gunsight: Fixed
* Canopy bug (can't close canopy): Bug hunting code adjusted further
* Logfile: log.lst is mandatory now (conf.ini settings are being overruled)
* Guided Missiles: Target acquisition code reworked to lower the performance impact in case of large number of targets
* Various loadout issues fixed
* He-219 Trigger assignment for "Schräge Musik" and Loadout Descriptions fixed
* Missing Translations for Controls GUI Interface ("Refresh" and "Input", in all available languages) added.
* Joystick Interface Bug fixed where Control Input was clipped when Axis was not symmetric and not mirrored either
* Right Mouse Button action (and others) restored in FMB
* Avia S-199 4x SC-50/70 bomb racks restored (Thanks to Knochenlutscher)
* McDonnell FH2 Banshee Cockpit position issue solved
* TBF/TBM Cockpit issue fixed, invisible gauges restored (Thans to Loku)
* Missing Ta 152 H-1 Nav Lights fixed
* Missing Radio Station Icon fixed
* Gun Hooks on Liberator GR.V ball turret fixed
* New Karelia Map restored (Thanks to Loku)
* Nakajima A2N2, A2N3 and A4N1 hooks partly realigned to enable normal takeoff behaviour (fixes nose-over issue)
* New Network Speed Settings: <1Mbit/s, 1-10MBit/2, 10-100MBit/s, >100MBit/s
* Further missing Translations for Controls GUI Interface added.
* FZG-76 spawn bug fixed
* 109/190 Mistel attachment bug fixed
* minor tweaks here and there
* "Hornets Nest" 3D fixes by Birdman implemented
* Me 155 B-2 and B-4 gunsight bug fixed
* Java 8 Disconnection log output toned down
* Fw 190 A-8 Mistel Bug fixed
* Mistel explosive load gets armed once Ju-88 is airborne now
* Net Replication for planes with more then 6 engines fixed
* Net Replication of long loadout lists fixed
* Ju-52 float version "hangs" in the water - fixed
* Bv-155 noses over on groundstart - fixed
* Ta-152C3 canopy movement speed is wrong - fixed
* J2M droptank rack missing in arming preview - fixed
* Hurricane IIb wing racks keep showing in arming preview even when no bombs loadout is selected - fixed
* P-61 Spawn Bug - fixed
* Erratic HUD log when B-36 AI planes spawn - fixed
* Various HUD related issues fixed
* XF5U prop damage issue fixed
* Improved code for scanning Aircraft skin folders (10 times faster than before)
* New SAS Common Utils implemented
* New IL-2 Selector implemented
* SAS Common Utils updated to 1.15
* Aircraft skins list is returned sorted / case insensitive now
* CTD when selecting "placeholder" (asterisk / *) lines from planes list in arming screens fixed.
* Java 8 and OpenJDK 17 JSGME mods removed
* MiG-17PF Radar works online now
* Missing Translations for new waypoint options in FMB fixed
* Map Coordinates in HUD are displayed correctly now even if the map is just barely large enough for 2-digit coords
* N1K flap freeze fixed
* IL-2 Graphics Extender crash fixed
* New Debug Code for ongoing canopy issues after mission rotation - in case you cannot close your canopy after mission start, please post your logfile contents in the bug report thread, thanks!
* Gladiator Flight Model issues fixed (by Loku)
* Erratic log output in "death while RRR" situations removed
* Ta-183 weapon hooks fixed
* Vildebeest Seaplane 10-bombs loadout fixed
* Venom pylon display fixed
* Superbolt Throttle Animation fixed
* P-61A missing turret fixed
* P-61 manifold pressure gauges fixed
* F6F-5N added
* AN/APS-6 Night Fighter Cockpits added, see https://www.sas1946.com/main/index.php/topic,70220.msg764828.html#msg764828
* F2H Banshee has 2 engines now
* Various MDS resource issues fixed
* Multiline Center HUD log implemented
* Curtiss model 64 Hawk II Gunsight Tubes fixed
* J4M1 & Ki-98 I blurry Cockpits fixed
* Various Fw-190 / Ta-152 Cockpit issues fixed
* IL-4 Top Gunner Cockpit texture issue: Codechange, please report back your results
* Missing Effects file added
* Diff Brake Code issue fixed (see Note² below)
* TBD-1A gunsight fixed
* Bf 109 B, C, D series reticle masks fixed (thanks FL!)
* FokkerDXVII Haken enabled code fixes
* FW 190 D-15 armament "2x MG 151/20" fixed
* Zenit61K_W texture issue fixed
* New Blenheim Pits with various fixes
* 262 top speeds fixed
* Ta 183 "Default" and "Empty" loadouts fixed
* Some weapon presets fixed (thx to FL2070!)
* Differential Brake Axis assignment fixed which could cause a crash in the Controls GUI menu, causing the game to lock up when trying to set Controls assignments
* Edge case fixed where misaligned cod loadouts could cause an airplane not to replicate over the net correctly

New features:
* AI can fly the mistel now, air spawn, ground start and GATTACK waypoints work now.
* AI handling of large formations improved, including fully working "straight in" landing pattern
* AI spawns in selected formation now
* C-46 added
* P-55A added
* Saab Tunnan added
* New Message Trigger Option added: Message Triggers can optionally apply only to the player who "triggered" the trigger now, i.e. all players get individual messages from such a message trigger.
* "Spawn" triggers now can be used to delayed-spawn aircraft according to the Start time set
* Player limit on Client hosted missions raised to 127
* Layered Forest doesn't cause immediate destruction when touching it's canopy anymore, instead planes can fly through the forest for a short period of time (depending on forst height, plane altitude, speed, plane size etc.) before the plane gets damaged and finally explodes
* DServer dumps a stacktrace on exit request now (random server crash bughunting)
* Animated FOV mod included, see notes below for conf.ini entries!
* Transparent "Test Runways" 4, 5 and 6 have been supplemented by 3 more versions without collision boxes, eliminating the "bump" bug when running across the edge of such runways.
* Names of these runways have been altered, see screenshot below.
* Note that in FMB you only get the icon of these new "collision-less" runways, nothing else, as there's nothing more to show. If you want to know the dimension of the regarding "test" runway, choose a version with collision box first, align it properly, and as the very last step, change it's type to a "collision-less" version.
* In FMB, when plane limits are enabled, an arbitrary number of loadouts can be selected per plane. The old limit of 9 loadouts doesn't exist anymore
* HUD Speedbar reworked based on "VerboseHudUnits" mod by FL2070
* Blenheim Mk.IV with chin turret available
* "Cross Airfield" takeoff prohibited with new code, see Note³

NOTE:

Starting with Hotfix 15, ignoring taxiways and runways on airports that have them (i.e. taking off "cross-airfield") is prohibited, provided difficulty settings (Vulnerability, Takeoff and Landings, Realistic Landings) are all set.
Keep this in mind next time when you lose your gear on takeoff.

NOTE²:

Due to changes to the AircraftHotkeys section, we kindly remind you to double-check your key and axis assignments for differential (left/right) brakes.
Key assignments can be found in the "Aircraft Controls" section on the Controls Setting GUI menu (~1/3 down the page), Axis in the "Hotas" section (down on the bottom)

Note³: These are the conf.ini entries for the animated FOV mod.
Shown here are the default values which apply if no other value is set.

[Mods]
FOVAnim = 1     // 1 enables animated FOV transition, 0 disables animation (which is like stock game, immediate FOV change unanimated)
FOVMin = 5      // Minimum FOV value in degrees
FOVMax = 175    // Maximum FOV value in degrees
FOVSpeed = 200  // Speed of FOV transition in percent. Default 200 means "2x speed" animation
FOVDefault = 70 // Default FOV value in degrees at mission start time
FOVLog = 0      // 1 enables logging of FOV changes, 0 disables it