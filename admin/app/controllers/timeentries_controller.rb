class TimeentriesController < ApplicationController
  def index
    @title = 'Working Time List'
    #puts "Trong TimeentriesController index id=#{params[:id]}"
    @id = params[:id]
    @user = User.find(params[:id])
    @timeentries = @user.timeentries
    @timeentry = @timeentries.first
    @timeentries.each { |te|
      te.exception = Tcexception.find(te.tcexception_id).value
    }
    #@timeentry.exception = "Hello Exception"
  end

  def get_time_entries
    @title = 'Working Time List'
    #puts "Trong TimeentriesController index id=#{params[:id]}"
    @user = User.find(params[:id])
    @timeentries = @user.timeentries

    xml_str = '<?xml version="1.0" encoding="UTF-8"?>'
    xml_str << "<rows>"
    @timeentries.each { |te|
      te.exception = Tcexception.find(te.tcexception_id).value

      xml_str << "<row id='#{te.id}'>"
      #  <page>1</page>
      #  <total>1</total>
      #  <records>1</records>
      xml_str << "<cell>#{te.date}</cell>"
      xml_str << "<cell>#{te.date}</cell>"
      xml_str << "<cell>#{formatted_hour(te.arrive_at)}</cell>"
      xml_str << "<cell>#{formatted_hour(te.out_at)}</cell>"
      xml_str << "<cell>#{formatted_hour(te.return_at)}</cell>"
      xml_str << "<cell>#{formatted_hour(te.leave_at)}</cell>"
      xml_str << "<cell>#{te.exception}</cell>"
      xml_str << "<cell>#{te.notes}</cell>"
      xml_str << '</row>'
    }

    xml_str << "</rows>"
    
    render :xml => xml_str
  end

  def tableedit
    puts "BBBBBBBB #{params}"
    #puts "BBBBBBBB #{params['oldValue']}"

    te_id = params[:id]
    old_te = Timeentry.find(te_id)

    # puts "old_te : #{old_te.attributes}"
    # puts "old_te : #{old_te.attributes.to_options!}"

    params.keys.each do |key|
      #puts key
      col_name = key
      #puts "Key: #{col_name}"
      if col_name == "tcexception"
        old_value = Tcexception.find(old_te.tcexception_id).value
      else
        old_value = old_te.attributes[col_name.to_sym]
      end
      
      new_value = params[col_name]

      #puts "#{old_value.blank?} || #{old_value.nil?} || #{old_value.class}"
      next if old_value.blank?
      #puts "Old value : #{old_value} ;; New value : #{new_value}"

      
      if (old_value != new_value)
        timeentry = Timeentry.find(te_id)
        if (col_name == 'arrive_at' || col_name == 'out_at' || col_name == 'return_at' ||
              col_name == 'leave_at')
          tz = Time.zone.parse(new_value)
          timeentry.update_attributes(col_name => tz.utc)
        else
          puts " timeentry.update_attributes #{col_name} : #{new_value}"
          timeentry.update_attributes(col_name => new_value)
        end

        # "id"=>"e897765e5e7b8d916703a673b95bedda",
        # "oldValue"=>"2010-09-06 07:01:59 UTC", "newValue"=>"2010-09-06 07:01:59 UTC",
        # "colName"=>":arrive_at", "controller"=>"timeentries", "action"=>"tableedit"
       
      end
    end
    render :text => 'OK' and return
    #render :text => 'Error'
  end

  def get_exceptions
    ex_all = Tcexception.all

    ex_all.each { |ex|
      result << ex.id << ":" << ex.value << ";"
    }

    result
  end
end