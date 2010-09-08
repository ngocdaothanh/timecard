class CsvController < ApplicationController
  #require 'rubygems'
  #require "fastercsv"

  def download

    @user = User.find(params[:id])
    @timeentries = @user.timeentries

    @outfile = "timeentries_" + Time.now.strftime("%m-%d-%Y") + ".csv"

    csv_data = CSV.generate do |csv|
      csv << [
        "Date",
        "Arrive At",
        "Out At",
        "Return At",
        "Leave At",
        "Notes"
      ]
      @timeentries.each do |te|
        csv << [
          te.date,
          te.arrive_at,
          te.out_at,
          te.return_at,
          te.leave_at,
          te.notes
        ]
      end
    end

    #    if request.env['HTTP_USER_AGENT'] =~ /msie/i
    #      headers['Pragma'] = 'public'
    #      headers["Content-type"] = "text/plain"
    #      headers['Cache-Control'] = 'no-cache, must-revalidate, post-check=0, pre-check=0'
    #      #headers['Content-Disposition'] = "attachment; filename=\"#{filename}\""
    #      headers['Expires'] = "0"
    #    else
    #      headers["Content-Type"] ||= 'text/csv'
    #      #headers["Content-Disposition"] = "attachment; filename=\"#{filename}\""
    #    end


    send_data csv_data,
      :type => 'text/csv; charset=iso-8859-1; header=present',
      :disposition => "attachment; filename=#{@outfile}"

    flash[:notice] = "Export complete!"

    # render :text => 'OK'
  end
end
