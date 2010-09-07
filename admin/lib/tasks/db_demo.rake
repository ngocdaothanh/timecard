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
        d_now = Date.new(2010, 9, 6)
        t_now = Time.now
        3.times do |t|
          Timeentry.create(:date => d_now + t,
               :arrive_at => t_now,
               :out_at => t_now + 3600*2,
               :return_at =>t_now + 3600*3,
               :leave_at => t_now + 3600*4,
             #  :tcexception => exception,
               :tcexception_id => exception.id,
               :notes => "Example notes",
               :user => user)
        end
        
      end
    end
  end
end
