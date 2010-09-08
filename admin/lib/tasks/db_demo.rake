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

    user_num = 0


    groupnames = ["Game", "RD"]
    groupnames.each do |gn|
      g = Group.create(:name => gn)

      puts "Group id : #{g.id}"

      2.times do |n|
        user_num += 1
        username = "user#{user_num}"
        user = User.create!(:username => username,
          :name => Faker::Name.name,
          :password =>"a",
          :group => g,
          :disabled => false
        )
        puts "-- User id : #{user.id}"

        # TimeEntry
        puts "Trong db_demo ------ Time zone : #{Time.zone}"
        d_now = Date.new(2010, 9, 7)
        t_now = Time.now
        3.times do |t|
          t = Timeentry.create(:date => d_now + t,
               :arrive_at => t_now,
               :out_at => t_now + 3600,
               :return_at =>t_now + 3600*2,
               :leave_at => t_now + 3600*3,
             #  :tcexception => exception,
               :tcexception_id => exception.id,
               :notes => "Example notes",
               :user => user)
             puts t.inspect
        end
      end
      #user2 is manager of user1
      user2 = User.find_by_username('user2')
      user1 = User.find_by_username('user1')
      user1.manager_id = user2.id
      user1.save
    end
  end
end
