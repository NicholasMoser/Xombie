using CrateModLoader.GameSpecific.WormsForts;
using CrateModLoader.GameSpecific.WormsForts.XOM;
using Microsoft.Win32;
using System.IO;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using static System.Net.Mime.MediaTypeNames;

namespace Xombie
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void btnOpenFile_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.InitialDirectory = "E:\\GNTLargeFiles\\Extracted\\Worms3D\\files";
            openFileDialog.Filter = "xom files (*.xom)|*.xom|All files (*.*)|*.*";
            if (openFileDialog.ShowDialog() == true)
            {
                XOM_TYPE.GetSupported();
                XOM_File file = new XOM_File();
                file.FileGame = WormsGame.W3D;
                file.Read(openFileDialog.FileName);
                // Save with file.Write(filePath);
                List<string> names = file.GetStrings();
                foreach (Container container in file.GetAllContainers())
                {
                    if (container is XColorResourceDetails colorResource)
                    {
                        string format = "XColorResourceDetails {0} R/G/B/A {1}/{2}/{3}/{4}\n";
                        string text = String.Format(format, colorResource.Name, colorResource.R, colorResource.G, colorResource.B, colorResource.A);
                        txtEditor.Text += text;
                    }
                    else if (container is XContainerResourceDetails containerResource)
                    {
                        string format = "XContainerResourceDetails {0} \n";
                        string text = String.Format(format, containerResource.Name);
                        txtEditor.Text += text;
                    }
                    else if (container is XFloatResourceDetails floatResource)
                    {
                        string format = "XFloatResourceDetails {0} {1}\n";
                        string text = String.Format(format, floatResource.Name, floatResource.Value);
                        txtEditor.Text += text;
                    }
                    else if (container is XIntResourceDetails intResource)
                    {
                        string format = "XIntResourceDetails {0} {1}\n";
                        string text = String.Format(format, intResource.Name, intResource.Value);
                        txtEditor.Text += text;
                    }
                    else if (container is XStringResourceDetails stringResource)
                    {
                        string format = "XStringResourceDetails {0} {1}\n";
                        string text = String.Format(format, stringResource.Name, stringResource.Value);
                        txtEditor.Text += text;
                    }
                    else if (container is XUintResourceDetails uintResource)
                    {
                        string format = "XUintResourceDetails {0} {1}\n";
                        string text = String.Format(format, uintResource.Name, uintResource.Value);
                        txtEditor.Text += text;
                    }
                    else if (container is XVectorResourceDetails vectorResource)
                    {
                        string format = "XVectorResourceDetails {0} X/Y/Z {1}/{2}/{3}\n";
                        string text = String.Format(format, vectorResource.Name, vectorResource.X, vectorResource.Y, vectorResource.Z);
                        txtEditor.Text += text;
                    }
                    else
                    {
                        txtEditor.Text += "UnknownContainer\n";
                    }
                }
            }
        }
    }
}