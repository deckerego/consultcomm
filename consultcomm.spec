Summary: Consultant Communicator (ConsultComm) is a program that allows anyone managing multiple projects, clients or tasks to effectively keep track of exactly how long they've spent on each project.
Name: CsltComm
Version: 2.4
Release: 1
Copyright: GPL
Group: Applications/Productivity
Source: CsltComm-2.4.src.tar
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
ant X
ant dist

%install
rm -rf $RPM_BUILD_ROOT
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm/X
mkdir -p $RPM_BUILD_ROOT/usr/local/ConsultComm/docs

install -v *.jar $RPM_BUILD_ROOT/usr/local/ConsultComm
install -v docs/* $RPM_BUILD_ROOT/usr/local/ConsultComm/docs
install -v X/libtimeout.so $RPM_BUILD_ROOT/usr/local/ConsultComm/X

%clean
rm -rf $RPM_BUILD_ROOT

%files
%defattr(-,root,root)
%doc README CHANGES COPYING AUTHORS

/usr/local/ConsultComm/CsltComm.jar
/usr/local/ConsultComm/skinlf.jar
/usr/local/ConsultComm/X/libtimeout.so
/usr/local/ConsultComm/docs/dbconnect.html
/usr/local/ConsultComm/docs/faq.html
/usr/local/ConsultComm/docs/index.html
/usr/local/ConsultComm/docs/install.html
/usr/local/ConsultComm/docs/using.html

%changelog
* Tue Oct 15 2002 John Ellis <john.ellis@ise-indy.com>
- initial release (build 1)
