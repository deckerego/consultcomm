Summary: Consultant Communicator (ConsultComm) is a program that allows anyone managing multiple projects, clients or tasks to effectively keep track of exactly how long they've spent on each project.
Name: ConsultComm
Version: 3beta1
Release: 1
Copyright: GPL
Group: Applications/Productivity
Source: ConsultComm-3beta1.src.tar
BuildRoot: /var/tmp/%{name}-buildroot

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
ant

%install
rm -rf $RPM_BUILD_ROOT
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm/plugins
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm/TimeOutBean
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm/JDBCBean
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm/docs
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm/icons
mkdir -p $RPM_BUILD_ROOT/usr/local/bin

install *.class $RPM_BUILD_ROOT/usr/local/ConsultComm
install ConsultComm $RPM_BUILD_ROOT/usr/local/bin
install docs/* $RPM_BUILD_ROOT/usr/local/ConsultComm/docs
install graphics/ConsultComm.xpm $RPM_BUILD_ROOT/usr/local/ConsultComm/icons
install plugins/*.jar $RPM_BUILD_ROOT/usr/local/ConsultComm/plugins

%clean
if [ $RPM_BUILD_ROOT != "/" ] ; then
  rm -rf $RPM_BUILD_ROOT
fi

%files
%defattr(-,root,root)
%doc README CHANGES COPYING AUTHORS

/usr/local/bin/ConsultComm
/usr/local/ConsultComm

%changelog
* Wed Oct 16 2002 John Ellis <john.ellis@ise-indy.com>
- Added shell script, icon, renamed package
* Tue Oct 15 2002 John Ellis <john.ellis@ise-indy.com>
- initial release (build 1)

