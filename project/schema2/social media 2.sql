CREATE TABLE `user` (
  `id` bigint UNIQUE PRIMARY KEY NOT NULL,
  `username` varchar(20) UNIQUE NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(30) NOT NULL,
  `hash_password` varchar(32) NOT NULL,
  `about_me` bigint NOT NULL,
  `lat` DECIMAL(8,6) DEFAULT 0,
  `lon` DECIMAL(9,6) DEFAULT 0,
  `post` bigint NOT NULL,
  `is_online` tinyint DEFAULT 0,
  `is_blocked` tinyint DEFAULT 0,
  `is_looking_someone_to_play_with` tinyint DEFAULT 1,
  `created_at` timestamp NOT NULL,
  `last_profile_update` timestamp NOT NULL,
  `last_post` timestamp,
  `last_instrument_offer` timestamp,
  `last_music_sheet` timestamp
);

CREATE TABLE `aboutme` (
  `id` bigint UNIQUE PRIMARY KEY NOT NULL,
  `description` text DEFAULT "",
  `instrument_interested_in` JSON,
  `created_at` timestamp NOT NULL,
  `last_update_at` timestamp NOT NULL
);

CREATE TABLE `posts` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `description` text DEFAULT "",
  `content` longblob NOT NULL,
  `posted_by` bigint NOT NULL,
  `created_at` timestamp NOT NULL,
  `last_update_at` timestamp NOT NULL,
  `author` varchar(255) DEFAULT "",
  `title` varchar(255) DEFAULT ""
);

CREATE TABLE `hashtag` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `used` bigint NOT NULL,
  `hashtag_ref` bigint
);

CREATE TABLE `secondhandinstruments` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `posted_by` bigint NOT NULL,
  `price` smallint NOT NULL,
  `instrument_description` text DEFAULT "",
  `created_at` timestamp NOT NULL,
  `last_update_at` timestamp NOT NULL
);

CREATE TABLE `follow` (
  `id` bigint UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `follower` bigint NOT NULL,
  `followed` bigint NOT NULL
);

CREATE TABLE `musicsheet` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `posted_by` bigint NOT NULL,
  `author` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT "",
  `sheet` longblob NOT NULL,
  `created_at` timestamp NOT NULL,
  `last_update_at` timestamp NOT NULL
);

CREATE TABLE `tag` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `tagged` bigint NOT NULL,
  `where` bigint NOT NULL
);

CREATE TABLE `taggedinstruments` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `name_id` bigint,
  `where` bigint NOT NULL
);

CREATE TABLE `searchplayer` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `searcher` bigint NOT NULL,
  `lat` DECIMAL(8,6) DEFAULT 0,
  `lon` DECIMAL(9,6) DEFAULT 0,
  `description` text DEFAULT ""
);

ALTER TABLE `aboutme` ADD FOREIGN KEY (`id`) REFERENCES `user` (`about_me`);

ALTER TABLE `posts` ADD FOREIGN KEY (`posted_by`) REFERENCES `user` (`id`);

ALTER TABLE `hashtag` ADD FOREIGN KEY (`used`) REFERENCES `posts` (`id`);

ALTER TABLE `hashtag` ADD FOREIGN KEY (`used`) REFERENCES `secondhandinstruments` (`id`);

ALTER TABLE `secondhandinstruments` ADD FOREIGN KEY (`posted_by`) REFERENCES `user` (`id`);

ALTER TABLE `follow` ADD FOREIGN KEY (`follower`) REFERENCES `user` (`id`);

ALTER TABLE `follow` ADD FOREIGN KEY (`followed`) REFERENCES `user` (`id`);

ALTER TABLE `hashtag` ADD FOREIGN KEY (`used`) REFERENCES `musicsheet` (`id`);

ALTER TABLE `musicsheet` ADD FOREIGN KEY (`posted_by`) REFERENCES `user` (`id`);

ALTER TABLE `tag` ADD FOREIGN KEY (`tagged`) REFERENCES `user` (`id`);

ALTER TABLE `tag` ADD FOREIGN KEY (`where`) REFERENCES `posts` (`id`);

ALTER TABLE `tag` ADD FOREIGN KEY (`where`) REFERENCES `musicsheet` (`id`);

ALTER TABLE `tag` ADD FOREIGN KEY (`where`) REFERENCES `secondhandinstruments` (`id`);

ALTER TABLE `taggedinstruments` ADD FOREIGN KEY (`where`) REFERENCES `posts` (`id`);

ALTER TABLE `taggedinstruments` ADD FOREIGN KEY (`where`) REFERENCES `user` (`id`);

ALTER TABLE `taggedinstruments` ADD FOREIGN KEY (`where`) REFERENCES `musicsheet` (`id`);

ALTER TABLE `taggedinstruments` ADD FOREIGN KEY (`where`) REFERENCES `secondhandinstruments` (`id`);

ALTER TABLE `taggedinstruments` ADD FOREIGN KEY (`where`) REFERENCES `searchplayer` (`id`);

ALTER TABLE `searchplayer` ADD FOREIGN KEY (`searcher`) REFERENCES `user` (`id`);
