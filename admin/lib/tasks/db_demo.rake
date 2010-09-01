# projects/lib/tasks/db_demo.rake
namespace :db do
  desc "Fill database with sample data"
  task :demo => :environment do
    require 'faker'

    10.times do |n|
      username = "user#{n+1}"
      User.create(:username => username,
          :name => Faker::Name.name,
          :password =>"a")
    end
  end
end
