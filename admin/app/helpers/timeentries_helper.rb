module TimeentriesHelper
  def formatted_hour(time)
    time.in_time_zone(Time.zone).to_formatted_s(:time)
    #time
  end

  def isManagerOf(user)
    manager_id = User.find(user.id).manager_id

    if (manager_id.nil? || current_user.nil?)
      puts "(manager_id.nil? || current_user.nil?)"
      return false
    end
    current_user.id == manager_id
  end
end
