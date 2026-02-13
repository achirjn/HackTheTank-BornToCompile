import './globals.css'

export const metadata = {
  title: 'HTT - Welcome',
  description: 'HTT Platform',
}

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className="bg-slate-50">{children}</body>
    </html>
  )
}
