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
end
