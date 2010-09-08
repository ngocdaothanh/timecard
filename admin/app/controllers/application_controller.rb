class ApplicationController < ActionController::Base
  protect_from_forgery
  include SessionsHelper
  include ApplicationHelper

  def must_login
    redirect_to :controller => "home", :action => "login"
  end

  def go_home
    redirect_to :controller => "home", :action => "index"
  end
end