module TimeentriesHelper
  def formatted_hour(time)
    time.in_time_zone(Time.zone).to_formatted_s(:time)
    #time
  end
end
