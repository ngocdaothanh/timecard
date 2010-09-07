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

    send_data csv_data,
      :type => 'text/csv; charset=iso-8859-1; header=present',
      :disposition => "attachment; filename=#{@outfile}"

    flash[:notice] = "Export complete!"

    # render :text => 'OK'
  end
end
