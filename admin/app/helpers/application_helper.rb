module ApplicationHelper
  def current_user
#    unless @current_user
#      @current_user = User.find(session['user_id'])
#    end
    @current_user ||= User.find(session['user_id'])
    @current_user
  end
  
  def is_manager_of(user)
    manager_id = User.find(user.id).manager_id

    if (manager_id.nil? || current_user.nil?)
      puts "(manager_id.nil? || current_user.nil?)"
      return false
    end
    current_user.id == manager_id
  end

  def is_admin(username)
    username = username.downcase
    begin
      file = File.new("#{Rails.root}/config/adminlist.adm", "r")
      while (line = file.gets)
        if username.eql?(line.chomp.downcase)
          return true
        end
      end
      file.close
    rescue => err
      puts "Exception: #{err}"
      err
    end
    false
  end

  def is_admin_
    session['is_admin']
  end

  def get_encrypt(s)
      s ||= ""
      Digest::SHA2.hexdigest(s)
  end

end
