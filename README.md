
## Bounties V2

Bounties v2 is a recoded version of [Bounties](https://github.com/SemiVanilla-MC/Bounties). This was a complete recode,
that means the entire plugin is again coded from the scratch.

The primary reason for the recode is because the orginal concept was a simple plugin, but later the features and requirements made it into an advanced plugin. For instance some of the reasons that made this recode necessary is that while i coded the base bounties bounties plugin, I did use the `Player` as reference in all the objects as the first concept was simpler, while later Combat logging was added which made the `Player` null on some situtations...Situations like this can be handled, but would result in [Spaghetti code](https://en.wikipedia.org/wiki/Spaghetti_code), which as a developer I shouldn't allow to happen. So some of the additional features that can happen in the recode is

    - Use `UUID` on Objects
    - Replace JSON With SQLite/H2 as I feel its not worth for 3 different JSON files, while SQLite is more perfomant in any case. (H2 is more advanced, latest and perfomant, but would need to learn)
    - Add Bounty Reward Queued on Combat Logged Situations (SQLite)
    - Add Support For [AureliumAPI](https://github.com/Archy-X/AureliumSkills/blob/master/src/main/java/com/archyx/aureliumskills/api/AureliumAPI.java) (Plan to make it future-proof, like would make it easier to hook up with any Other plugin if Aurelium Skills need to be replaced later)
    - Create an API and hook [SquareMapPlayer](https://github.com/SemiVanilla-MC/SquaremapPlayers) with it
    - Replace the complex reward system with a simpler system of FXP due to queuing and combat logging
    - Add a anti-spam functionality where basically if a player killed same player repeatedly, cancel everything related to bounty. (To Me: Keep a new player Death Listener, and create a new Cached Map  with specified time limit (60 sec).When a player killed a player, add them both if again killed the same guy, cancel the bounty rewards/kills, if the dead guy is different, continue the reward system, while adding the new dead guy to the map. Don't remove this even if the killer gets killed)


