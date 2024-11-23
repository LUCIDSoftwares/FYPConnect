CREATE DATABASE FYPC;
USE FYPC;

create table User(
	ID int primary key auto_increment,
    name varchar(25) not null,
    password varchar(25) not null,
    usertype varchar(10) not null,	# can be student/ faculty/ admin
    cgpa double,			# student
    username varchar(25) not null unique,  # faculty and admin, will be roll num in case of student
    email varchar(40) not null unique	
);
    
    # drop table user
    
create table project(
	ID int primary key auto_increment,
    title varchar(150) not null,
    description varchar(2000) not null,
    doc_link varchar(100),
    faculty_ID int,
    FOREIGN KEY (faculty_ID) REFERENCES User(ID)
    );

	# drop table project
    
create table groupT(
	ID int primary key auto_increment,
    name varchar(50) not null,
    leader int not null,
    student1 int,
    student2 int,
    projectID int,
    FOREIGN KEY (leader) references User(ID),
    FOREIGN KEY (student1) references User(ID),
    FOREIGN KEY (student2) references User(ID),
    FOREIGN KEY (projectID) references project(ID)
    );
    
    # drop table groupT
    
    
create table Gr_Inv(   #group invitation
	ID int primary key auto_increment,
    GroupID int not null,
    Stud_ID int not null, # reciever student
    Status varchar(10),    # accepted or declined or pending
    FOREIGN KEY (GroupID) references groupT(ID),
    FOREIGN KEY (stud_ID) references User(ID)
    );
    
    # drop table Gr_Inv
    
create table Gr_Req(	#group requests
	ID int primary key auto_increment,
    GroupID int not null,
    Stud_ID int not null, # sender student
    Status varchar(10),    # accepted or declined or pending 	
    FOREIGN KEY (GroupID) references groupT(ID),
    FOREIGN KEY (stud_ID) references User(ID)
    );
    
    # drop table Gr_Req
    
    
create table Ment_Req(	#mentorship requests
	ID int primary key auto_increment,
    GroupID int not null,
    Project_ID int not null, # sender student
	Status varchar(10),    # accepted or declined or pending
    FOREIGN KEY (GroupID) references groupT(ID),
    FOREIGN KEY (Project_ID) references project(ID)
    );
    
    # drop table Ment_Req 
    
create table Deliverable( 
	ID int primary key auto_increment,
    Deadline datetime,
    description varchar(2000) not null,
    doc_link varchar(50),
    facultyID int not null,
    foreign key (facultyID) references User(ID)
    );
    
    # drop table Deliverable
    
create table Submission(
	ID int primary key auto_increment,
	Del_ID int not null, 	# Deliverable ID
    submission_time time not null,
    groupID int not null,
    content_link varchar(100),
    foreign key (Del_ID) references Deliverable(ID),
    foreign key (groupID) references GroupT(ID)
    );
    
    # drop table Submission
    
create table Feedback(
	ID int primary key auto_increment,
    Group_ID int not null,
    Faculty_ID int not null,
    Submission_ID int not null,
    Grade varchar(3) not null,
    Remarks varchar(100),
    
    foreign key (Group_ID) references GroupT(ID),
    foreign key (Faculty_ID) references User(ID),
    foreign key (Submission_ID) references submission(ID)
    );
    
    # drop table Feedback

CREATE TABLE Resource(
	ID int primary key auto_increment,
    title varchar(50) not null,
    description varchar(100) not null,
    uploader_username varchar(25) not null,
    filePath varchar(100) not null,
    
    FOREIGN KEY (uploader_username) REFERENCES User(username)
);

    # drop table Resource