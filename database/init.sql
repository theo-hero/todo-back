CREATE TABLE IF NOT EXISTS "Tasks" (
	"id" serial NOT NULL UNIQUE,
	"description" varchar(200) NOT NULL,
	"created_by" bigint NOT NULL,
	"team_id" bigint,
	"status" varchar(32) NOT NULL DEFAULT 'active',
	"created_at" timestamp NOT NULL DEFAULT NOW(),
	"estimated_time" varchar(255) NOT NULL,
	"deadline" timestamp DEFAULT NULL, 
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Users" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(100) NOT NULL,
	"nickname" varchar(40) NOT NULL,
	"email" varchar(254) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Teams" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(200) NOT NULL,
	"user_ids" INT[] NOT null,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Intervals" (
	"id" serial NOT NULL UNIQUE,
	"task_of_focus" bigint NOT NULL,
	"start" timestamp NOT NULL,
	"end" timestamp NOT NULL,
	"user_id" bigint NOT NULL,
	"rate" numeric(2, 1) NOT NULL DEFAULT 5.0 check (rate between 0.0 and 5.0),
	PRIMARY KEY ("id")
);

ALTER TABLE "Tasks" ADD CONSTRAINT "Tasks_fk2" FOREIGN KEY ("created_by") REFERENCES "Users"("id");

ALTER TABLE "Tasks" ADD CONSTRAINT "Tasks_fk3" FOREIGN KEY ("team_id") REFERENCES "Teams"("id");

ALTER TABLE "Intervals" ADD CONSTRAINT "Intervals_fk1" FOREIGN KEY ("task_of_focus") REFERENCES "Tasks"("id");

ALTER TABLE "Intervals" ADD CONSTRAINT "Intervals_fk4" FOREIGN KEY ("user_id") REFERENCES "Users"("id");

ALTER TABLE tasks ADD CONSTRAINT tasks_status_check CHECK (status IN ('active','completed','archived'));