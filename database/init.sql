CREATE TABLE IF NOT EXISTS "tasks" (
	"id" serial NOT NULL UNIQUE,
	"description" varchar(200) NOT NULL,
	"created_by" bigint NOT NULL,
	"team_id" bigint,
	"status" varchar(255) NOT NULL,
	"created_at" timestamp with time zone NOT NULL,
	"deadline" timestamp with time zone,
	"estimated_time" bigint,
	"duration" bigint,
	"project_id" bigint,
	"difficulty" bigint,
	"urgency" bigint,
	"importance" bigint,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "users" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(100) NOT NULL,
	"username" varchar(40) NOT NULL UNIQUE,
	"email" varchar(254) NOT NULL UNIQUE,
	"password" varchar(150) NOT NULL,
	"enabled" boolean NOT NULL DEFAULT true,
	"last_login_at" timestamp with time zone,
	"failed_attempts" bigint DEFAULT 0,
	PRIMARY KEY ("id")
);

create table if not exists telegram_links (
  user_id       bigint references users(id) unique,
  telegram_id   bigint unique not null,
  linked_at     timestamptz not null,
  active       boolean default true
)

CREATE TABLE IF NOT EXISTS "teams" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(200) NOT NULL,
	"created_by" bigint,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "intervals" (
	"id" serial NOT NULL UNIQUE,
	"task_of_focus" bigint,
	"start" timestamp with time zone NOT NULL,
	"end" timestamp with time zone,
	"user_id" bigint NOT NULL,
	"rate" bigint,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "team_members" (
	"team_id" bigint NOT NULL UNIQUE,
	"user_id" bigint NOT NULL UNIQUE,
	PRIMARY KEY ("team_id", "user_id")
);

CREATE TABLE IF NOT EXISTS "projects" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(100) NOT NULL,
	"created_by" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "task_tags" (
	"task_id" bigint NOT NULL,
	"tagname" varchar(50) NOT NULL,
	PRIMARY KEY ("task_id", "tagname")
);

CREATE TABLE IF NOT EXISTS "user_settings" (
	"user_id" bigint NOT NULL UNIQUE,
	"remind_time" bigint,
	"default_interval_minutes" bigint NOT NULL DEFAULT '30',
	"teams_enabled" boolean NOT NULL DEFAULT '0',
	"sharing_enabled" boolean NOT NULL DEFAULT '0',
	PRIMARY KEY ("user_id")
);

CREATE TABLE IF NOT EXISTS "authorities" (
	"username" varchar(40),
	"authority" varchar(40),
	PRIMARY KEY ("username", "authority")
);

ALTER TABLE "tasks" ADD CONSTRAINT "Tasks_fk2" FOREIGN KEY ("created_by") REFERENCES "users"("id") ON DELETE CASCADE;
ALTER TABLE "tasks" ADD CONSTRAINT "Tasks_fk3" FOREIGN KEY ("team_id") REFERENCES "teams"("id") ON DELETE SET NULL;
ALTER TABLE "tasks" ADD CONSTRAINT "Tasks_fk9" FOREIGN KEY ("project_id") REFERENCES "projects"("id") ON DELETE SET NULL;
ALTER TABLE "tasks" ADD CONSTRAINT tasks_status_check CHECK (status IN ('active','completed','archived'));

ALTER TABLE "intervals" ADD CONSTRAINT "Intervals_fk1" FOREIGN KEY ("task_of_focus") REFERENCES "tasks"("id") ON DELETE CASCADE;
ALTER TABLE "intervals" ADD CONSTRAINT "Intervals_fk4" FOREIGN KEY ("user_id") REFERENCES "users"("id") ON DELETE CASCADE;

ALTER TABLE "teams" ADD CONSTRAINT "Teams_fk0" FOREIGN KEY ("created_by") REFERENCES "users"("id") ON DELETE SET NULL;

ALTER TABLE "team_members" ADD CONSTRAINT "Team_members_fk0" FOREIGN KEY ("team_id") REFERENCES "teams"("id") ON DELETE CASCADE;
ALTER TABLE "team_members" ADD CONSTRAINT "Team_members_fk1" FOREIGN KEY ("user_id") REFERENCES "users"("id") ON DELETE CASCADE;

ALTER TABLE "projects" ADD CONSTRAINT "Projects_fk2" FOREIGN KEY ("created_by") REFERENCES "users"("id") ON DELETE CASCADE;

ALTER TABLE "task_tags" ADD CONSTRAINT "TaskTags_fk1" FOREIGN KEY ("task_id") REFERENCES "tasks"("id") ON DELETE CASCADE;

ALTER TABLE "user_settings" ADD CONSTRAINT "UserSettings_fk0" FOREIGN KEY ("user_id") REFERENCES "users"("id") ON DELETE CASCADE;

ALTER TABLE "authorities" ADD CONSTRAINT "Authorities_fk0" FOREIGN KEY ("username") REFERENCES "users"("username") ON UPDATE CASCADE ON DELETE CASCADE;

create unique index "ix_auth_username" on "authorities" ("username", "authority");