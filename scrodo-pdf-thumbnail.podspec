require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "scrodo-pdf-thumbnail"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = "https://github.com/TOURE003/scrodo-pdf-thumbnail"
  s.license      = "MIT"
  s.authors      = { "Scrodo" => "contact@scrodo.com" }
  s.platforms    = { :ios => "13.0" }
  s.source       = { :git => "https://github.com/TOURE003/scrodo-pdf-thumbnail.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,mm,swift}"

  s.dependency "React-Core"

  s.frameworks = "PDFKit"

  s.pod_target_xcconfig = {
    'SWIFT_VERSION' => '5.0',
    'DEFINES_MODULE' => 'YES'
  }
end
