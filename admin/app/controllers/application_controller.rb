class ApplicationController < ActionController::Base
  protect_from_forgery

  def must_login
    #redirect_to ':controller => "home", :action => "login"'
    redirect_to home_login_path
  end

  def go_home
    #redirect_to :controller => "home", :action => "index"
    redirect_to home_index_path
  end

  def authenticate
    session['is_authenticated'] == 'authenticated'
  end

  def get_encrypt(s)
      s ||= ""
      Digest::SHA2.hexdigest(s)
  end
end
