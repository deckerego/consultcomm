Summary: Consultant Communicator (ConsultComm) is a program that allows anyone managing multiple projects, clients or tasks to effectively keep track of exactly how long they've spent on each project.
Name: ConsultComm
Version: 3.2beta1
Release: 1
Copyright: GPL
Group: Applications/Productivity
Source: ConsultComm-%{version}.src.tar
BuildRoot: /var/tmp/%{name}-buildroot
BuildRequires: ant

%description
Consultant Communicator (ConsultComm) is a small, lightweight, 
platform-independent program written in Java that allows anyone managing 
multiple projects, clients or tasks to effectively keep track of exactly 
how long they've spent on each project. It's been widely used by 
consultants in my company... and saved me the grief of remembering the 
gazillion projects I work on during the course of a day. 

JDBC support is included so you can instantly upload all your times and 
projects into most JDBC and ODBC compliant databases. Your elapsed times 
and preferences are also stored in XML, so you can query/modify the data 
all you want. 

%prep
%setup

%build
ant build

%install
rm -rf $RPM_BUILD_ROOT
mkdir -p $RPM_BUILD_ROOT/usr/share/ConsultComm
mkdir -p $RPM_BUILD_ROOT/usr/share/ConsultComm/lib
mkdir -p $RPM_BUILD_ROOT/usr/share/ConsultComm/plugins
mkdir -p $RPM_BUILD_ROOT/usr/share/ConsultComm/icons
mkdir -p $RPM_BUILD_ROOT/usr/share/ConsultComm/syslibs
mkdir -p $RPM_BUILD_ROOT/usr/share/applications
mkdir -p $RPM_BUILD_ROOT/usr/bin

install ConsultComm/ConsultComm $RPM_BUILD_ROOT/usr/bin
install ConsultComm/graphics/*.xpm $RPM_BUILD_ROOT/usr/share/ConsultComm/icons
install ConsultComm/plugins/*.jar $RPM_BUILD_ROOT/usr/share/ConsultComm/plugins
install ConsultComm/lib/*.jar $RPM_BUILD_ROOT/usr/share/ConsultComm/lib
install ConsultComm/*.jar $RPM_BUILD_ROOT/usr/share/ConsultComm
install ConsultComm/help/faq.html $RPM_BUILD_ROOT/usr/share/ConsultComm/FAQ.html
install ConsultComm/ConsultComm.desktop $RPM_BUILD_ROOT/usr/share/applications

chmod a+w $RPM_BUILD_ROOT/usr/share/ConsultComm/syslibs

%clean
if [ $RPM_BUILD_ROOT != "/" ] ; then
  rm -rf $RPM_BUILD_ROOT
fi

%files
%defattr(-,root,root)
%doc ConsultComm/README ConsultComm/CHANGES ConsultComm/COPYING ConsultComm/AUTHORS

/usr/bin/ConsultComm
/usr/share/ConsultComm
/usr/share/applications

%changelog
* Mon Feb 28 2005 John Ellis <jtellis@users.sourceforge.net>
- Added group time total in project list
- Added antialiasing preference for fonts in project list
- If an alias does not exist in the JDBC plugin, the project name is exported
- Rebuilt and rewrote the timeout Win32 native class for TimeOut plugin using gcc and MinGW
- Window events are now broadcast to plugins; EmptyBean updated to demonstrate
- SysTray plugin now remembers if window was iconified or not
- Plugins now can add to project list pop-up menu
- Added easy copy-and-pasting for projects
- Fixed "Refresh" command in JDBC plugin when changing table or database URL
- Fixed relative positioning of popup windows
- Fixed help window aesthetics
* Wed Sep 27 2004 John Ellis <jtellis@users.sourceforge.net>
- Fixed bug where adding a new project while the timer was running would reset the selected project's elapsed time
- Added project name to $PROMPT dialog in JDBC plugin
- User can now select the Java installed look-and-feel in the preferences panel
- If a native library already exists when a plugin is trying to extract it, the existing plugin is deleted
- Build structure updated for use with NetBeans 4.0
- Binaries compiled using Java 1.5, JRE 1.4 is still the target platform (1.4 and 1.5 installations can run ConsultComm without problems)
* Mon Sep 20 2004 John Ellis <jtellis@users.sourceforge.net>
- Created plugin to display icon in system tray
- Records & prefs are saved whenever a project in the list is changed
- Added GUI installers for Win32 and other platforms with IzPack
- Added PROMPT and GLOBALPROMPT variables for JDBC plugin; allows for additional fields whose values are defined by prompting the user during database export
- Improved icon graphics
- Code cleanup for plugin architecture and extra metadata checking (version numbers, loaded status, etc.)
- Updated documentation
- Updated libraries for SkinLF and JavaHelp
- Fixed TableTree layout to solve problems with OS X Swing implementation
- Fixed interoperability with windows (tested with Windows XP SP2)
- Fixed bad layout on TimeOut plugin management page
- Fixed bug where timer would stop advancing project time after jumping in/out of a plugin
- Fixed plugin loader so it did not reinstantiate a plugin if it was already loaded
- Fixed ConsultComm crash when times total index was out of bounds
- Fixed JDBC plugin crash when no records were loaded
- Fixed rendering bugs where the time cell of the table tree didn't respond to mouse events
* Fri Jan 02 2004 John Ellis <jtellis@users.sourceforge.net>
- Sometimes fields would be stored out-of-order, made INSERT explicity define field names
- Cleaned up changelog
* Tue Nov 04 2003 John Ellis <jtellis@users.sourceforge.net>
- Fixed crash when closing standalone JDBC Customizer windows
- When a valid record isn't selected crashes don't occur
- Preemptively fixed a weird GUI resizing bug
* Thu Oct 23 2003 John Ellis <jtellis@users.sourceforge.net>
- Fixed conversion process with BILLABLE and DATE flags
* Fri Oct 10 2003 John Ellis <jtellis@users.sourceforge.net>
- Catch ClassCastException
- Recompiled Win32 .dll
* Tue Jul 01 2003 John Ellis <jtellis@users.sourceforge.net>
- Java 1.4 required
- Added JavaBeans based plugin architecture for new features and third-party extensions
- New GUI layout: TableTrees allow projects to be listed by group in a tree format that can be collapsed or expanded
- Moved help documentation to JavaHelp display
- Changed prefs file formatting
- Added ability to specify whether or not JDBC should specify the data table in the driver's URL
- JDBC driver (.jar or .zip) can be specified from the preferences panel instead of being explicitly defined in the classpath or extensions directory
- Updated to Skin Look and Feel 1.2.3
- Prefs for ConsultComm and plugins based in XML
- Projects stored in seperate XML file to query
- Rewrite of (nearly) the entire codebase to make things easier to understand and to fix a handfull of bugs
