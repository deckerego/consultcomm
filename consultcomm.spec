Summary: Consultant Communicator (ConsultComm) is a program that allows anyone managing multiple projects, clients or tasks to effectively keep track of exactly how long they've spent on each project.
Name: ConsultComm
Version: 3.1beta2
Release: 1
Copyright: GPL
Group: Applications/Productivity
Source: ConsultComm-%{version}.src.tar
BuildRoot: /var/tmp/%{name}-buildroot
BuildRequires: jakarta-ant

%description
Consultant Communicator (ConsultComm) is a small, lightweight, 
platform-independant program written in Java that allows anyone managing 
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
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm/plugins
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm/icons
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm/syslibs
mkdir -p $RPM_BUILD_ROOT/usr/local/bin

install ConsultComm/ConsultComm $RPM_BUILD_ROOT/usr/local/bin
install ConsultComm/graphics/*.xpm $RPM_BUILD_ROOT/usr/local/ConsultComm/icons
install ConsultComm/plugins/*.jar $RPM_BUILD_ROOT/usr/local/ConsultComm/plugins
install ConsultComm/*.jar $RPM_BUILD_ROOT/usr/local/ConsultComm

chmod a+w $RPM_BUILD_ROOT/usr/local/ConsultComm/syslibs

%clean
if [ $RPM_BUILD_ROOT != "/" ] ; then
  rm -rf $RPM_BUILD_ROOT
fi

%files
%defattr(-,root,root)
%doc ConsultComm/README ConsultComm/CHANGES ConsultComm/COPYING ConsultComm/AUTHORS

/usr/local/bin/ConsultComm
/usr/local/ConsultComm

%changelog
* Wed Feb 13 2003 John Ellis <john.ellis@ise-indy.com>
- Removed graphics directory (loaded in JAR)
* Wed Feb 3 2003 John Ellis <john.ellis@ise-indy.com>
- Added helpfile directory
* Wed Jan 29 2003 John Ellis <john.ellis@ise-indy.com>
- Modified directory structure for ease in building
* Wed Jan 22 2003 John Ellis <john.ellis@ise-indy.com>
- Added files & directories for plugin architecture
* Wed Oct 16 2002 John Ellis <john.ellis@ise-indy.com>
- Added shell script, icon, renamed package
* Tue Oct 15 2002 John Ellis <john.ellis@ise-indy.com>
- initial release (build 1)

