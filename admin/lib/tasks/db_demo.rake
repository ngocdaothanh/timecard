# projects/lib/tasks/db_demo.rake
namespace :db do
  desc "Fill database with sample data"
  task :demo => :environment do
    require 'faker'

    exceptions = ["tau tre", "di muon", "nghi phep"]
    exceptions.each do |ex|
      Tcexception.create(:value => ex)
    end

    exception = Tcexception.find_by_value("tau tre")

    groupnames = ["Game", "RD"]
    groupnames.each do |gn|
      g = Group.create(:name => gn)

      puts "Group id : #{g.id}"

      1.times do |n|
        username = "user#{n+1}"
        user = User.create!(:username => username,
          :name => Faker::Name.name,
          :password =>"a",
          :group => g
        )
        puts "-- User id : #{user.id}"

        # TimeEntry
        Timeentry.create(:date => Date.new(2010,10,05), 
               :arrive_at => Time.now,
               :out_at => Time.now,
               :return_at => Time.now,
               :leave_at => Time.now,
             #  :tcexception => exception,
               :tcexception_id => exception.id,
               :notes => "Example notes",
               :user => user)
      end
    end
  end
end
