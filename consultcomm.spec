Summary: Consultant Communicator (ConsultComm) is a program that allows anyone managing multiple projects, clients or tasks to effectively keep track of exactly how long they've spent on each project.
Name: ConsultComm
Version: 3.1rc2
Release: 1
Copyright: GPL
Group: Applications/Productivity
Source: ConsultComm-%{version}.src.tar
BuildRoot: /var/tmp/%{name}-buildroot
BuildRequires: jakarta-ant

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
ant X

%install
rm -rf $RPM_BUILD_ROOT
mkdir -p $RPM_BUILD_ROOT/usr/share/ConsultComm
mkdir -p $RPM_BUILD_ROOT/usr/share/ConsultComm/plugins
mkdir -p $RPM_BUILD_ROOT/usr/share/ConsultComm/icons
mkdir -p $RPM_BUILD_ROOT/usr/share/ConsultComm/syslibs
mkdir -p $RPM_BUILD_ROOT/usr/share/applications
mkdir -p $RPM_BUILD_ROOT/usr/bin

install ConsultComm/ConsultComm $RPM_BUILD_ROOT/usr/bin
install ConsultComm/graphics/*.xpm $RPM_BUILD_ROOT/usr/share/ConsultComm/icons
install ConsultComm/plugins/*.jar $RPM_BUILD_ROOT/usr/share/ConsultComm/plugins
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
* Mon Sep 13 2004 John Ellis <john.ellis@ise-indy.com>
    - Created plugin to display icon in system tray
    - Fixed TableTree layout to solve problems with OS X Swing implementation
    - Records & prefs are saved whenever a project in the list is changed
    - Added GUI installers for Win32 and other platforms with IzPack
    - Fixed interoperability with windows (tested with Windows XP SP2)
    - Fixed bad layout on TimeOut plugin management page
    - Fixed bug where timer would stop advancing project time after jumping 
      in/out of a plugin
    - Added PROMPT and GLOBALPROMPT variables for JDBC plugin; allows for 
      additional fields whose values are defined by prompting the user during 
      database export
    - Improved icon graphics
    - Code cleanup for plugin architecture and extra metadata checking (version 
      numbers, loaded status, etc.)
    - Fixed plugin loader so it did not reinstantiate a plugin if it was
      already loaded
    - Updated documentation
    - Updated libraries for SkinLF and JavaHelp
* Fri Jan 02 2004 John Ellis <john.ellis@ise-indy.com>
    - Sometimes fields would be stored out-of-order, made INSERT explicity 
      define field names
    - Cleaned up changelog
* Tue Nov 04 2003 John Ellis <john.ellis@ise-indy.com>
    - Fixed crash when closing standalone JDBC Customizer windows
    - When a valid record isn't selected crashes don't occur
    - Preemptively fixed a weird GUI resizing bug
* Thu Oct 23 2003 John Ellis <john.ellis@ise-indy.com>
    - Fixed conversion process with BILLABLE and DATE flags
* Fri Oct 10 2003 John Ellis <john.ellis@ise-indy.com>
    - Catch ClassCastException
    - Recompiled Win32 .dll
* Tue Jul 01 2003 John Ellis <john.ellis@ise-indy.com>
    - Java 1.4 required
    - Added JavaBeans based plugin architecture for new features and 
      third-party extensions
    - New GUI layout: TableTrees allow projects to be listed by group in a tree 
      format that can be collapsed or expanded
    - Moved help documentation to JavaHelp display
    - Changed prefs file formatting
    - Added ability to specify whether or not JDBC should specify the data 
      table in the driver's URL
    - JDBC driver (.jar or .zip) can be specified from the preferences panel 
      instead of being explicitly defined in the classpath or extensions 
      directory
    - Updated to Skin Look and Feel 1.2.3
    - Prefs for ConsultComm and plugins based in XML
    - Projects stored in seperate XML file to query
    - Rewrite of (nearly) the entire codebase to make things easier to 
      understand and to fix a handfull of bugs
