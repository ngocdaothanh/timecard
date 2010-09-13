module TimeentriesHelper
  def formatted_hour(time)
    time.in_time_zone(Time.zone).to_formatted_s(:time)
    #time
  end

   def get_exceptions
    ex_all = Tcexception.all
    result = ""
    ex_all.each { |ex|
      result << ex.id << ":" << ex.value << ";"
    }

    result.chop
  end
end
