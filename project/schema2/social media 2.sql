CREATE TABLE `user` (
  `id` bigint UNIQUE PRIMARY KEY NOT NULL,
  `username` varchar(20) UNIQUE NOT NULL,
  `email` varchar(30) NOT NULL,
  `hash_password` varchar(32) NOT NULL,
  `is_online` tinyint DEFAULT 0,
  `is_blocked` tinyint DEFAULT 0,
  `is_looking_someone_to_play_with` tinyint DEFAULT 1,
  `name` varchar(255) NOT NULL,
  `lat` DECIMAL(8,6) DEFAULT 0,
  `lon` DECIMAL(9,6) DEFAULT 0,
  `description` text DEFAULT "",
  `created_at` timestamp DEFAULT (now()),
  `last_post` timestamp DEFAULT (now()),
  `last_instrument_offer` timestamp DEFAULT (now()),
  `last_music_sheet` timestamp DEFAULT (now())
);

CREATE TABLE `follow` (
  `id` bigint UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `follower` bigint NOT NULL,
  `followed` bigint NOT NULL
);

CREATE TABLE `posts` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `author` varchar(255) DEFAULT "",
  `title` varchar(255) DEFAULT "",
  `description` text DEFAULT "",
  `content` longblob NOT NULL,
  `posted_by` bigint NOT NULL,
  `likes` bigint DEFAULT 0,
  `created_at` timestamp DEFAULT (now()),
  `last_update_at` timestamp DEFAULT (now())
);

CREATE TABLE `secondhandinstruments` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `posted_by` bigint NOT NULL,
  `price` smallint NOT NULL,
  `instrument_description` text DEFAULT "",
  `created_at` timestamp DEFAULT (now()),
  `last_update_at` timestamp DEFAULT (now())
);

CREATE TABLE `musicsheet` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `posted_by` bigint NOT NULL,
  `author` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT "",
  `sheet` longblob NOT NULL,
  `created_at` timestamp DEFAULT (now()),
  `last_update_at` timestamp DEFAULT (now())
);

CREATE TABLE `hashtag` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `hashtag_name` text NOT NULL,
  `hashtag_in` bigint NOT NULL
);

CREATE TABLE `tag` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `tagged` bigint,
  `tagged_in` bigint NOT NULL
);

CREATE TABLE `taggedinstruments` (
  `id` bigint UNIQUE PRIMARY KEY AUTO_INCREMENT,
  `instrument_name` text NOT NULL,
  `tagged_in` bigint NOT NULL
);

ALTER TABLE `follow` ADD FOREIGN KEY (`follower`) REFERENCES `user` (`id`);

ALTER TABLE `follow` ADD FOREIGN KEY (`followed`) REFERENCES `user` (`id`);

ALTER TABLE `musicsheet` ADD FOREIGN KEY (`posted_by`) REFERENCES `user` (`id`);

ALTER TABLE `tag` ADD FOREIGN KEY (`tagged`) REFERENCES `user` (`id`);

ALTER TABLE `secondhandinstruments` ADD FOREIGN KEY (`posted_by`) REFERENCES `user` (`id`);

ALTER TABLE `posts` ADD FOREIGN KEY (`posted_by`) REFERENCES `user` (`id`);
