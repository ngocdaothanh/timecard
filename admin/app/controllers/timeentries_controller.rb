class TimeentriesController < ApplicationController
  def index
    #puts "Trong TimeentriesController index id=#{params[:id]}"
    @user = User.find(params[:id])
    @timeentries = @user.timeentries
    @timeentry = @timeentries.first
    @timeentries.each { |te|
      te.exception = Tcexception.find(te.tcexception_id).value
    }
    #@timeentry.exception = "Hello Exception"
  end

  def tableedit
    puts "BBBBBBBB #{params}"
    puts "BBBBBBBB #{params['oldValue']}"

    colName = params['colName']
    oldValue = params['oldValue']
    newValue = params['newValue']

    unless (oldValue == newValue)
      timeentry = Timeentry.find( params[:id] )
      tz = Time.zone.parse(newValue)
      timeentry.update_attributes(colName => newValue)
      # "id"=>"e897765e5e7b8d916703a673b95bedda",
      # "oldValue"=>"2010-09-06 07:01:59 UTC", "newValue"=>"2010-09-06 07:01:59 UTC",
      # "colName"=>":arrive_at", "controller"=>"timeentries", "action"=>"tableedit"

      render :text => 'OK' and return
    end


    render :text => 'Error'
  end

end
