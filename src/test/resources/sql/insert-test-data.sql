INSERT INTO course_settings (id,start_date,end_date,is_public)
VALUES ('38811913-ce40-4ef4-a596-f24d94356949','2025-05-13 09:30:00','2025-06-23 15:00:00',true),
       ('34b56d95-5d74-44b1-b473-3ab2fd36cc5f','2025-05-01 00:00:00','2025-06-01 00:00:00',true);

INSERT INTO course (id,title,description,price,coins_paid,settings_id)
VALUES ('64852c52-ed64-4438-b095-2ca10f6b4be0','Applied Math','This course is essential for understanding math',245,2450,'38811913-ce40-4ef4-a596-f24d94356949'),
       ('b902261b-d1b9-4c58-869f-b04a5bbff4c9','Test course 1','No description needed',100,1000,'34b56d95-5d74-44b1-b473-3ab2fd36cc5f');

INSERT INTO student (id,first_name,last_name,email,date_of_birth,coins)
VALUES ('5a231280-1988-410f-98d9-852b8dc9caf1','Abap','Abapov','abap@gmail.com','2000-05-13',1540),
       ('8ce93381-f58d-4563-8866-34a0ed878c74','Ha','Ha','templmail1@gmail.com','2001-05-06',10000);

INSERT INTO lesson (id,title,duration,course_id)
VALUES ('373e60bb-c872-4207-982c-859b4dfdb4f7','Linear algebra',80,'64852c52-ed64-4438-b095-2ca10f6b4be0'),
       ('c6d8f322-83db-481f-ac1b-91d9e5093dc3','Lesson 1',90,'64852c52-ed64-4438-b095-2ca10f6b4be0');

INSERT INTO video_lesson (id, url, platform)
VALUES ('373e60bb-c872-4207-982c-859b4dfdb4f7', 'some-url', 'Zoom');

INSERT INTO classroom_lesson (id, location, capacity)
VALUES ('c6d8f322-83db-481f-ac1b-91d9e5093dc3', 'BSU', 25);