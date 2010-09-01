# projects/lib/tasks/db_demo.rake
namespace :db do
  desc "Fill database with sample data"
  task :demo => :environment do
    require 'faker'

    groupnames = ["Game", "RD"]
    groupnames.each do |gn|
      g = Group.create(:name => gn)

      puts "Group id : #{g.id}"

      3.times do |n|
        username = "user#{n+1}"
        user = User.create!(:username => username,
          :name => Faker::Name.name,
          :password =>"a",
          :group => g
        )
        puts "-- User id : #{user.id}"
      end
    end
  end
end
