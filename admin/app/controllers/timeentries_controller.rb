class TimeentriesController < ApplicationController
  before_filter :set_title

  def set_title
    @title = 'Working Time List'
  end

  def index
    #puts "Trong TimeentriesController index id=#{params[:id]}"
    @user = User.find(params[:id])
    #    @timeentries = @user.timeentries
    #    @timeentry = @timeentries.first
    #    @timeentries.each { |te|
    #      te.exception = Tcexception.find(te.tcexception_id).value unless te.tcexception_id.nil?
    #    }
    #@timeentry.exception = "Hello Exception"
  end

  def get_time_entries
    #puts "Trong TimeentriesController get_time_entries index id=#{params[:id]}"
    @user = User.find(params[:id])
    @timeentries = @user.timeentries

    xml_str = '<?xml version="1.0" encoding="UTF-8"?>'
    xml_str << "<rows>"
    @timeentries.each { |te|
      te.exception = get_exception(te.tcexception_id)

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
    #puts "BBBBBBBB #{params}"
    #puts "BBBBBBBB #{params['oldValue']}"

    te_id = params[:id]
    old_te = Timeentry.find(te_id)

    # puts "old_te : #{old_te.attributes}"
    # puts "old_te : #{old_te.attributes.to_options!}"

    params.keys.each do |key|
      col_names = ["date", "arrive_at", "out_at", "return_at", "leave_at", "tcexception_id", "notes"]
      time_col_names = ["arrive_at", "out_at", "return_at", "leave_at"]
      #puts key
      col_name = key
      next unless col_names.include?(col_name)
      
      #puts "Key: #{col_name}"
      if col_name == "date"
        old_value = old_te.attributes[col_name.to_sym].to_s
      elsif time_col_names.include?(col_name)
          old_value = formatted_hour(old_te.attributes[col_name.to_sym])
      else
        old_value = old_te.attributes[col_name.to_sym]
      end
      
      new_value = params[col_name]

      #puts "#{old_value.blank?} || #{old_value.nil?} || #{old_value.class}"
      next if old_value.blank? && new_value.blank?
      #puts "Old value : #{old_value} ;; New value : #{new_value}"

      if (old_value != new_value)
        timeentry = Timeentry.find(te_id)
        if time_col_names.include?(col_name)
          tz = Time.zone.parse(new_value)
          if tz.nil?
            #puts " timeentry.update_attributes #{col_name} : nil"
            timeentry.update_attributes(col_name => nil)
          else
            #puts " timeentry.update_attributes #{col_name} : #{tz.utc}"
            timeentry.update_attributes(col_name => tz.utc)
          end
        else
          #puts " timeentry.update_attributes #{col_name} : #{new_value}"
          timeentry.update_attributes(col_name => new_value)
        end
      end
    end
    render :text => 'OK' and return
    #render :text => 'Error'
  end
end