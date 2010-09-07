class ApplicationController < ActionController::Base
  protect_from_forgery

  def must_login
    redirect_to :controller => "home", :action => "login"
  end

  def go_home
    redirect_to :controller => "home", :action => "index"
  end

  def authenticate
    session['is_authenticated'] == 'authenticated'
  end

  def get_encrypt(s)
      s ||= ""
      Digest::SHA2.hexdigest(s)
  end

  def is_admin_
    session['is_admin']
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

end