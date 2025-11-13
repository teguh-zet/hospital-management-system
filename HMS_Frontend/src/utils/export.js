import { jsPDF } from 'jspdf'
import 'jspdf-autotable'

export const exportToPDF = (title, columns, data, filename = 'report') => {
  const doc = new jsPDF()
  
  // Add title
  doc.setFontSize(18)
  doc.text(title, 14, 22)
  
  // Add date
  doc.setFontSize(10)
  doc.text(`Generated on: ${new Date().toLocaleDateString('id-ID')}`, 14, 30)
  
  // Add table
  doc.autoTable({
    head: [columns],
    body: data,
    startY: 35,
    styles: { fontSize: 9 },
    headStyles: { fillColor: [52, 152, 219] }
  })
  
  // Save PDF
  doc.save(`${filename}_${new Date().getTime()}.pdf`)
}

export const exportToCSV = (title, columns, data, filename = 'report') => {
  // Create CSV content
  let csvContent = title + '\n'
  csvContent += `Generated on: ${new Date().toLocaleDateString('id-ID')}\n\n`
  csvContent += columns.join(',') + '\n'
  
  data.forEach(row => {
    csvContent += row.join(',') + '\n'
  })
  
  // Create blob and download
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  link.setAttribute('href', url)
  link.setAttribute('download', `${filename}_${new Date().getTime()}.csv`)
  link.style.visibility = 'hidden'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

export const printTable = (title, columns, data) => {
  const printWindow = window.open('', '_blank')
  const html = `
    <!DOCTYPE html>
    <html>
    <head>
      <title>${title}</title>
      <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        h1 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #3498db; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
        @media print {
          body { margin: 0; }
        }
      </style>
    </head>
    <body>
      <h1>${title}</h1>
      <p>Generated on: ${new Date().toLocaleDateString('id-ID')}</p>
      <table>
        <thead>
          <tr>
            ${columns.map(col => `<th>${col}</th>`).join('')}
          </tr>
        </thead>
        <tbody>
          ${data.map(row => `
            <tr>
              ${row.map(cell => `<td>${cell}</td>`).join('')}
            </tr>
          `).join('')}
        </tbody>
      </table>
    </body>
    </html>
  `
  printWindow.document.write(html)
  printWindow.document.close()
  printWindow.print()
}

